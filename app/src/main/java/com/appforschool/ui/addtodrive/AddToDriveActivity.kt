package com.appforschool.ui.addtodrive

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import com.appforschool.utils.AlertDialogUtility
import com.appforschool.utils.ImageFilePath
import com.appforschool.utils.LogM
import com.appforschool.utils.toast
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.opensooq.supernova.gligar.GligarPicker
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_add_to_drive.*
import java.io.File
import javax.inject.Inject

class AddToDriveActivity : BaseBindingActivity<ActivityAddToDriveBinding>() {

    override fun layoutId() = R.layout.activity_add_to_drive

    @Inject
    lateinit var viewModel: AddToDriveViewModel

    //Multiple Image selection START
    private val alMultiImage: ArrayList<Uri> = arrayListOf();
    private var selected_image = 1;
    //Multiple Image selection END

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
    }

    private val onMessageErrorObserver = Observer<Any> {
        toast(it.toString())
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
            when (selected_image) {

            }

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
            setSubjectAdapter(it)
        } else {
            toast(it!!.message)
        }
    }

    private fun setSubjectAdapter(it: SubjectListModel) {
        val adapter = SubjectAdapter(this@AddToDriveActivity, it.data)
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
            AlertDialogUtility.CustomAlert(this@AddToDriveActivity, getString(R.string.app_name), getString(R.string.select_file),
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
                handleCropResult(data!!)
            }
            else -> {
                handleCropError(data!!)
            }
        }
    }

    private fun uploadMultiImageFile(data: Intent?) {
        val imagesList = data?.extras?.getStringArray(GligarPicker.IMAGES_RESULT)
        val selectedUri = Uri.fromFile(File(imagesList?.get(0)))
        for (j in imagesList!!.indices) {
            val mUri = Uri.fromFile(File(imagesList?.get(j)))
            alMultiImage.add(mUri)
        }

        if (selectedUri != null) {
            selected_image = 1
            startCrop(selectedUri)
        } else {
            Toast.makeText(
                this@AddToDriveActivity,
                "R.string.toast_cannot_retrieve_selected_image",
                Toast.LENGTH_SHORT
            ).show()
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

    private fun uploadMultiImage(file: File) {
        viewModel.file.value = file
        val fileSizeInBytes = file.length()
        val fileSizeInKB = fileSizeInBytes / 1024
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(file.toString())
        val strFileExtension: String = "." + fileExtension
        viewModel.filesize.value = fileSizeInKB.toString()
        viewModel.fileext.value = strFileExtension
        viewModel.callFileAddDrive()
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
            Log.e("=> ", "handleCropError: ", cropError)
            Toast.makeText(this@AddToDriveActivity, cropError.message, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this@AddToDriveActivity, "Something went wrong", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun handleCropResult(result: Intent) {
        val resultUri = UCrop.getOutput(result)
        if (resultUri != null) {
            when (selected_image) {
                1 -> {
                    val file: File = File(resultUri.path)
                    uploadMultiImage(file)
                    selected_image = 2
                    if (alMultiImage.size > 1) {
                        startCrop(alMultiImage.get(1))
                    }
                }
                2 -> {
                    val file: File = File(resultUri.path)
                    uploadMultiImage(file)
                    uploadMultiImage(file)
                    selected_image = 3
                    if (alMultiImage.size > 2) {
                        startCrop(alMultiImage.get(2))
                    }
                }
                3 -> {
                    val file: File = File(resultUri.path)
                    uploadMultiImage(file)
                    uploadMultiImage(file)
                    selected_image = 4
                    if (alMultiImage.size > 3) {
                        startCrop(alMultiImage.get(3))
                    }
                }
                4 -> {
                    val file: File = File(resultUri.path)
                    uploadMultiImage(file)
                    uploadMultiImage(file)
                    selected_image = 5
                    if (alMultiImage.size > 4) {
                        startCrop(alMultiImage.get(4))
                    }
                }
            }
        } else {
            Toast.makeText(
                this@AddToDriveActivity,
                "R.string.toast_cannot_retrieve_cropped_image",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    //Multiple Image selection END
}