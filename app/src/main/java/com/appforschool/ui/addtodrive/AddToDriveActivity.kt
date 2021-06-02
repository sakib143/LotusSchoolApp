package com.appforschool.ui.addtodrive

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.lifecycle.Observer
import com.appforschool.R
import com.appforschool.base.BaseBindingActivity
import com.appforschool.data.model.*
import com.appforschool.databinding.ActivityAddToDriveBinding
import com.appforschool.listner.UserProfileListner
import com.appforschool.ui.addtodrive.adapter.KnowledgeSpinnerAdapter
import com.appforschool.ui.addtodrive.adapter.StandardAdapter
import com.appforschool.ui.addtodrive.adapter.SubjectAdapter
import com.appforschool.utils.*
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.opensooq.supernova.gligar.GligarPicker
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_add_to_drive.*
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class AddToDriveActivity : BaseBindingActivity<ActivityAddToDriveBinding>() {

    override fun layoutId() = R.layout.activity_add_to_drive

    @Inject
    lateinit var viewModel: AddToDriveViewModel

    //Multiple Image selection START
    private val alMultiImage: ArrayList<MultiImageModel> = arrayListOf();
    private var selected_image = 1;
    //Multiple Image selection END

    //Add to PDF START
    private var file: File? = null
    private var fileOutputStream: FileOutputStream? = null
    private var pdfDocument = PdfDocument();
    //Add to PDF END

    private var alSubjectList: ArrayList<SubjectListModel.Data> = ArrayList<SubjectListModel.Data>()

    override fun initializeBinding(binding: ActivityAddToDriveBinding) {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        binding.listner = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setObserver()
        viewModel.setFileSelect(true)
        viewModel.checkUserType()
        setKnowledgeSpinner()
        file = getOutputFile()
        fileOutputStream = FileOutputStream(file)
    }

    private fun setKnowledgeSpinner() {
        val adapter = KnowledgeSpinnerAdapter(this@AddToDriveActivity, viewModel.alKnowledge)
        spKnowledgeAD.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun intentFor(
            context: Context
        ) = Intent(context, AddToDriveActivity::class.java)

    }

    private fun setObserver() {
        viewModel.onMessageError.observe(this, onMessageErrorObserver)
        viewModel.standard.observe(this, standardObserver)
        viewModel.subject.observe(this, subjectObserver)
        viewModel.uploadFileLink.observe(this, uploadFileLinkObserver)
        viewModel.upload_selected_file.observe(this, fileUploadingObserver)
        viewModel.showDialogForFile.observe(this,showDialogForFileObserver)
    }

    private val onMessageErrorObserver = Observer<Any> {
        toast(it.toString())
    }

    private val showDialogForFileObserver = Observer<Boolean> {
        AlertDialogUtility.showConfirmAlert(this@AddToDriveActivity, this@AddToDriveActivity.getString(R.string.continue_without_attachment)) {
                dialog, which ->
            dialog.dismiss()
            viewModel.callWithoutFileAddDrive()
        }
    }

    private val standardObserver = Observer<StandardListModel> {
        if (it.status) {
            setStandardSpinner(it)
        } else {
            toast(it!!.message)
        }
    }

    private val uploadFileLinkObserver = Observer<UploadFileUrlModel> {
        if (it.status) {
            toast(it!!.message)
        } else {
            toast(it!!.message)
        }
    }

    private val fileUploadingObserver = Observer<AssignmentSubmissionModel> {
        if (it.status) {
            toast(it!!.message)
        } else {
            toast(it!!.message)
        }
    }

    private fun setStandardSpinner(standardListModel: StandardListModel) {
        val adapter = StandardAdapter(this@AddToDriveActivity, viewModel.alStandardList)
        spStandardAD.adapter = adapter
    }

    private val subjectObserver = Observer<SubjectListModel> {
        if (it.status) {
            alSubjectList.clear()
            alSubjectList  = it.data
            setSubjectAdapter()
        } else {
            alSubjectList.clear()
            setSubjectAdapter()
            toast(it!!.message)
        }
    }

    private fun setSubjectAdapter() {
        val adapter = SubjectAdapter(this@AddToDriveActivity, alSubjectList)
        spSubjectAD.adapter = adapter
    }

    override fun onBackPressed() {
        closeScreen()
    }

    fun closeScreen() {
        UserProfileListner.getInstance().changeState(true)
        finish()
    }

    fun chooseFile() {
        checkFileSubmitPermission()
    }

    fun checkFileSubmitPermission() =
        runWithPermissions(Manifest.permission.READ_EXTERNAL_STORAGE) {
            AlertDialogUtility.CustomAlert(this@AddToDriveActivity,
                getString(R.string.app_name),
                getString(R.string.select_file),
                getString(R.string.image_file_title),
                getString(R.string.other_file_title),
                { dialog, which ->
                    dialog.dismiss()
                    GligarPicker().limit(4).disableCamera(false).cameraDirect(false).requestCode(
                        MULTI_IMAGE_PICKER_RESULT_CODE
                    ).withActivity(this).show()
                },
                { dialog, which ->
                    dialog.dismiss()
                    var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
                    chooseFile.setType("*/*")
                    chooseFile = Intent.createChooser(chooseFile, "Choose a file")
                    startActivityForResult(chooseFile, PICKFILE_RESULT_CODE)
                })
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PICKFILE_RESULT_CODE -> {
                val filePath = ImageFilePath.getPath(this@AddToDriveActivity, data?.data)
                val file: File = File(filePath)
                uploadSingleFile(file)
            }
            MULTI_IMAGE_PICKER_RESULT_CODE -> {
                uploadMultiImageFile(data)
            }
            UCrop.REQUEST_CROP -> {
                if(data != null) {
                    handleCropResult(data)
                }
            }
            else -> {
                handleCropError(data!!)
            }
        }
    }

    private fun uploadMultiImageFile(data: Intent?) {
        val imagesList = data?.extras?.getStringArray(GligarPicker.IMAGES_RESULT)
        val selectedUri = Uri.fromFile(File(imagesList?.get(0)))
        alMultiImage.clear()
        for (j in imagesList!!.indices) {
            val mUri = Uri.fromFile(File(imagesList?.get(j)))
            alMultiImage.add(MultiImageModel(mUri, false))
        }

        if (selectedUri != null) {
            selected_image = 1
            startCrop(selectedUri)
        } else {
            toast("Can not retrieve selected images")
        }
    }

    private fun uploadSingleFile(file: File) {
        viewModel.file.value = file
        val fileSizeInBytes = file.length()
        val fileSizeInKB = fileSizeInBytes / 1024
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(file.toString())
        val strFileExtension: String = "." + fileExtension
        viewModel.filesize.value = fileSizeInKB.toString()
        viewModel.fileext.value = strFileExtension
    }

    //Multiple Image selection START
    private fun startCrop(uri: Uri) {
        val options = UCrop.Options()
        options.setFreeStyleCropEnabled(true)
        options.setHideBottomControls(true)

        val destinationFileName = ".jpg"
        val uCrop = UCrop.of(uri, Uri.fromFile(File(cacheDir, destinationFileName)))
        uCrop.withOptions(options)
        uCrop.start(this@AddToDriveActivity)
    }

    private fun handleCropError(result: Intent) {
        val cropError = UCrop.getError(result)
        if (cropError != null) {
            toast(cropError.message!!)
        } else {
            toast(resources.getString(R.string.something_went_wrong))
        }
    }

    private fun handleCropResult(result: Intent) {
        val resultUri = UCrop.getOutput(result)
        if (resultUri != null) {
            LogM.e("Cropped Images path is " + resultUri.path)
            when (selected_image) {
                1 -> {
                    setFirstImage(resultUri)
                }
                2 -> {
                    setSecondImage(resultUri)
                }
                3 -> {
                    setThirdImage(resultUri)
                }
                4 -> {
                    setFourthImage(resultUri)
                }
            }
        } else {
            toast(getString(R.string.try_again_crop))
        }
    }

    private fun setFourthImage(resultUri: Uri) {
        selected_image = 5
        alMultiImage.get(3).imageUri = resultUri
        alMultiImage.get(3).isSelected = true
        addToPdf(3)
        if (alMultiImage.size > 4) {
            startCrop(alMultiImage.get(4).imageUri)
        }
    }

    private fun setThirdImage(resultUri: Uri) {
        selected_image = 4
        alMultiImage.get(2).imageUri = resultUri
        alMultiImage.get(2).isSelected = true
        addToPdf(2)
        if (alMultiImage.size > 3) {
            startCrop(alMultiImage.get(3).imageUri)
        }
    }

    private fun setSecondImage(resultUri: Uri) {
        selected_image = 3
        alMultiImage.get(1).imageUri = resultUri
        alMultiImage.get(1).isSelected = true
        addToPdf(1)
        if (alMultiImage.size > 2) {
            startCrop(alMultiImage.get(2).imageUri)
        }
    }

    private fun setFirstImage(resultUri: Uri) {
        selected_image = 2
        alMultiImage.get(0).imageUri = resultUri
        alMultiImage.get(0).isSelected = true
        addToPdf(0)
        if (alMultiImage.size > 1) {
            startCrop(alMultiImage.get(1).imageUri)
        }
    }

    fun addToPdf(position: Int) {
        var pageNumber = position
        pageNumber += 1
        val firstBitmap = BitmapFactory.decodeFile(alMultiImage.get(position).imageUri.path)
        val decoded: Bitmap = globalMethods.getResizedBitmap(firstBitmap,720)!!  //Compress Image file to reduce pdf size
        val pageInfo = PdfDocument.PageInfo.Builder(decoded.width, decoded.height, pageNumber).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page?.canvas
        val paint = Paint()
        paint.setColor(Color.BLUE)
        canvas?.drawPaint(paint)
        canvas?.drawBitmap(decoded, 0f, 0f, null)
        pdfDocument.finishPage(page)
        decoded.recycle()
        pdfDocument.writeTo(fileOutputStream)
        uploadSingleFile(file!!)
    }

    private fun getOutputFile(): File? {
        val root = File(getExternalFilesDir(null), "WhiteBoardLab")
        var isFolderCreated = true
        if (!root.exists()) {
            isFolderCreated = root.mkdir()
        }

        return if (isFolderCreated) {
            File(root, "whiteboard_images.pdf")
        } else {
            Toast.makeText(this, "Folder is not created", Toast.LENGTH_SHORT).show()
            null
        }
    }
    //Multiple Image selection END
}