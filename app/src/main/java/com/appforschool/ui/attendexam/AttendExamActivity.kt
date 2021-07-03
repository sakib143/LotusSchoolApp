package com.appforschool.ui.attendexam

import android.Manifest
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.appforschool.R
import com.appforschool.base.BaseBindingActivity
import com.appforschool.data.model.*
import com.appforschool.databinding.ActivityAttendExamBinding
import com.appforschool.listner.UserProfileListner
import com.appforschool.ui.full_image.FullImageActivity
import com.appforschool.utils.*
import com.google.gson.JsonObject
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import com.opensooq.supernova.gligar.GligarPicker
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.activity_attend_exam.*
import kotlinx.android.synthetic.main.fragment_subjects.*
import java.io.File
import java.io.FileOutputStream
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class AttendExamActivity : BaseBindingActivity<ActivityAttendExamBinding>() {


    override fun layoutId() = R.layout.activity_attend_exam

    @Inject
    lateinit var viewModel: AttendExamViewModel

    private var alAttendExam: ArrayList<AttendExamModel.Data> = ArrayList()
    private var adapter: AttendExamAdapter? = null
    private var binding: ActivityAttendExamBinding? = null
    private var oldAnswer: String = ""

    //Multiple Image selection START
    private val alMultiImage: ArrayList<MultiImageModel> = arrayListOf();
    private var selected_image = 1;
    //Multiple Image selection END

    //Add to PDF START
    var file: File? = null
    var fileOutputStream: FileOutputStream? = null
    val pdfDocument = PdfDocument()
    //Add to PDF END

    override fun initializeBinding(binding: ActivityAttendExamBinding) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.listner = this
        this.binding = binding
        binding.alAttendExam = alAttendExam
    }

    private var strExamId: String? = null
    private var strQuestionId: String? = null
    private var position: Int? = null
    private var isFromViewResult: Boolean = false
    private var totalobtainedmarks: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        file = getOutputFile()
        fileOutputStream = FileOutputStream(file)

        isFromViewResult = intent.getBooleanExtra(Constant.REQUEST_MODE_VIEW_RESULT, false)
        strExamId = intent.getStringExtra(Constant.REQUEST_EXAM_ID)!!
        totalobtainedmarks = intent.getStringExtra(Constant.KEY_OBTAIN_MARKS)!!

        if (isFromViewResult) {
            tvSubmit.setText(resources.getString(R.string.close))
            val marksForResult =
                totalobtainedmarks + " / " + intent.getStringExtra(Constant.KEY_MAKRS)
            setPercent()
            viewModel.setData(
                intent.getStringExtra(Constant.REQUEST_EXAM_ID)!!,
                intent.getStringExtra(Constant.REQUEST_GET_EXAMS)!!,
                intent.getStringExtra(Constant.REUQEST_GET_SUBJECTS)!!,
                marksForResult,
                intent.getStringExtra(Constant.KEY_DURATION)!!,
                intent.getStringExtra(Constant.KEY_TIME)!!,
                intent.getStringExtra(Constant.KEY_FORMATED_TIME)!!,
                intent.getBooleanExtra(Constant.REQUEST_MODE_VIEW_RESULT, false)
            )
        } else {
            viewModel.setData(
                intent.getStringExtra(Constant.REQUEST_EXAM_ID)!!,
                intent.getStringExtra(Constant.REQUEST_GET_EXAMS)!!,
                intent.getStringExtra(Constant.REUQEST_GET_SUBJECTS)!!,
                intent.getStringExtra(Constant.KEY_MAKRS)!!,
                intent.getStringExtra(Constant.KEY_DURATION)!!,
                intent.getStringExtra(Constant.KEY_TIME)!!,
                intent.getStringExtra(Constant.KEY_FORMATED_TIME)!!,
                intent.getBooleanExtra(Constant.REQUEST_MODE_VIEW_RESULT, false)
            )
        }

        setData()
    }

    private fun setPercent() {
        //Getting percentage.
        val totalMark: Double = intent.getStringExtra(Constant.KEY_MAKRS)!!.toDouble()
        val percentage: Double = (100 * totalobtainedmarks.toDouble()) / totalMark
        viewModel.setPercentage(percentage.toString())
    }

    companion object {
        @JvmStatic
        fun intentFor(
            context: Context,
            examId: String,
            examName: String,
            subject: String,
            makrs: String,
            duration: String,
            time: String,
            formatedTime: String,
            isFromResult: Boolean,
            totalobtainedmarks: String
        ) =
            Intent(context, AttendExamActivity::class.java)
                .putExtra(Constant.REQUEST_EXAM_ID, examId)
                .putExtra(Constant.REQUEST_GET_EXAMS, examName)
                .putExtra(Constant.REUQEST_GET_SUBJECTS, subject)
                .putExtra(Constant.KEY_MAKRS, makrs)
                .putExtra(Constant.KEY_DURATION, duration)
                .putExtra(Constant.KEY_TIME, time)
                .putExtra(Constant.KEY_FORMATED_TIME, formatedTime)
                .putExtra(Constant.REQUEST_MODE_VIEW_RESULT, isFromResult)
                .putExtra(Constant.KEY_OBTAIN_MARKS, totalobtainedmarks)
    }

    private fun setData() {
        viewModel.onMessageError.observe(this, onMessageErrorObserver)
        viewModel.attend_exam.observe(this, attendExamObserver)
        viewModel.setFiveMinuteLeft.observe(this, onFiveMinuteLeft)
        viewModel.setTimeOver.observe(this, onTimeOverObserver)
        viewModel.endExam.observe(this, endExamObserver)
        viewModel.uploadAnswer.observe(this, uploadAnswerFileObserver)

        viewModel.executeAttentExamList() //API call to fetch list

        /*
            If user come from view result then fiveMinuteLeftAlert, oneMinuteLeftAlert and timeOverAlert
            dialogs will not be apear and will not execute any API anymore.
         */
        if (isFromViewResult) {

        } else {
            viewModel.fiveMinuteLeftAlert()
            viewModel.oneMinuteLeftAlert()
            viewModel.timeOverAlert()
        }
    }

    private fun setFiveMinRemainAlert() {
        AlertDialogUtility.showSingleAlert(
            this@AttendExamActivity, getString(R.string.exam_min_left_message)
        ) { dialog, which ->
            dialog.dismiss()
        }
    }

    private val onMessageErrorObserver = Observer<Any> {
        toast(it.toString())
    }

    private val onTimeOverObserver = Observer<String> {
        executeEndExamAPI()
        closeScreen()
    }

    private val onFiveMinuteLeft = Observer<String> {
        setFiveMinRemainAlert()
    }

    private val attendExamObserver = Observer<AttendExamModel> {
        if (it.status) {
            alAttendExam = ArrayList()
            alAttendExam!!.addAll(it!!.data!!)
            binding?.alAttendExam = alAttendExam
            if (alAttendExam?.size == 0) {
                viewModel.setDataFound(false)
            } else {
                viewModel.setDataFound(true)
                rvAttendExam.setHasFixedSize(true)
                rvAttendExam.layoutManager = LinearLayoutManager(this@AttendExamActivity)
                adapter = AttendExamAdapter(this@AttendExamActivity, it.data, isFromViewResult)
                rvAttendExam.adapter = adapter
            }
        } else {
            toast(it!!.message)
            viewModel.setDataFound(true)
        }
    }

    private val uploadAnswerFileObserver = Observer<UploadAnswerFileModel> {
        if (it.status) {
            toast(it.message)
        } else {
            toast(it!!.message)
            viewModel.setDataFound(true)
        }
    }


    private val endExamObserver = Observer<EndExamModel> {
        if (it.status) {
            toast(it!!.data.get(0).message)
            closeScreen()
        } else {
            toast(it!!.message)
        }
    }

    fun zoomImage(imageUrl: String, image: ImageView) {
        ViewCompat.setTransitionName(image, Constant.IMAGE_FULL_ZOOM_ANIM)
        val intent = Intent(this, FullImageActivity::class.java)
        intent.putExtra(Constant.REQUEST_LINK_URL, imageUrl)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            image!!,
            ViewCompat.getTransitionName(image)!!
        )
        startActivity(intent, options.toBundle())
    }

    fun optionAClicked(position: Int, srNumber: String) {
        val inputParam = JsonObject()
        if (alAttendExam.get(position).isACorrect) {
            alAttendExam.get(position).isACorrect = false
            inputParam.addProperty(Constant.REQUEST_OPTION_A_VALUE, "0")
        } else {
            alAttendExam.get(position).isACorrect = true
            inputParam.addProperty(Constant.REQUEST_OPTION_A_VALUE, "1")
        }
        alAttendExam.get(position).isBCorrect = false
        alAttendExam.get(position).isCCorrect = false
        alAttendExam.get(position).isDCorrect = false
        adapter?.notifyDataSetChanged()


        inputParam.addProperty(Constant.REQUEST_MODE, Constant.REQUEST_UPDATE_EXAM_ANSWERS)
        inputParam.addProperty(Constant.REUQEST_USER_ID, prefUtils.getUserData()?.userid)
        inputParam.addProperty(Constant.REQUEST_STUDENTID, prefUtils.getUserData()?.studentId)
        inputParam.addProperty(Constant.REQUEST_SR_NO, alAttendExam.get(position).srNo)

        inputParam.addProperty(Constant.REQUEST_OPTION_B_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_OPTION_C_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_OPTION_D_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_SUBJECTIVE_ANSWER, "")
        viewModel.executeUpdateExamAnswer(inputParam)
    }

    fun optionBClicked(position: Int, srNumber: String) {
        val inputParam = JsonObject()
        alAttendExam.get(position).isACorrect = false
        if (alAttendExam.get(position).isBCorrect) {
            alAttendExam.get(position).isBCorrect = false
            inputParam.addProperty(Constant.REQUEST_OPTION_B_VALUE, "0")
        } else {
            alAttendExam.get(position).isBCorrect = true
            inputParam.addProperty(Constant.REQUEST_OPTION_B_VALUE, "1")
        }
        alAttendExam.get(position).isCCorrect = false
        alAttendExam.get(position).isDCorrect = false
        adapter?.notifyDataSetChanged()

        inputParam.addProperty(Constant.REQUEST_MODE, Constant.REQUEST_UPDATE_EXAM_ANSWERS)
        inputParam.addProperty(Constant.REUQEST_USER_ID, prefUtils.getUserData()?.userid)
        inputParam.addProperty(Constant.REQUEST_STUDENTID, prefUtils.getUserData()?.studentId)
        inputParam.addProperty(Constant.REQUEST_SR_NO, alAttendExam.get(position).srNo)
        inputParam.addProperty(Constant.REQUEST_OPTION_A_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_OPTION_C_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_OPTION_D_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_SUBJECTIVE_ANSWER, "")
        viewModel.executeUpdateExamAnswer(inputParam)
    }

    fun optionCClicked(position: Int, srNumber: String) {
        val inputParam = JsonObject()
        alAttendExam.get(position).isACorrect = false
        alAttendExam.get(position).isBCorrect = false
        if (alAttendExam.get(position).isCCorrect) {
            alAttendExam.get(position).isCCorrect = false
            inputParam.addProperty(Constant.REQUEST_OPTION_C_VALUE, "0")
        } else {
            alAttendExam.get(position).isCCorrect = true
            inputParam.addProperty(Constant.REQUEST_OPTION_C_VALUE, "1")
        }
        alAttendExam.get(position).isDCorrect = false
        adapter?.notifyDataSetChanged()


        inputParam.addProperty(Constant.REQUEST_MODE, Constant.REQUEST_UPDATE_EXAM_ANSWERS)
        inputParam.addProperty(Constant.REUQEST_USER_ID, prefUtils.getUserData()?.userid)
        inputParam.addProperty(Constant.REQUEST_STUDENTID, prefUtils.getUserData()?.studentId)
        inputParam.addProperty(Constant.REQUEST_SR_NO, alAttendExam.get(position).srNo)
        inputParam.addProperty(Constant.REQUEST_OPTION_A_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_OPTION_B_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_OPTION_D_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_SUBJECTIVE_ANSWER, "")
        viewModel.executeUpdateExamAnswer(inputParam)
    }

    fun optionDClicked(position: Int, srNumber: String) {
        val inputParam = JsonObject()
        alAttendExam.get(position).isACorrect = false
        alAttendExam.get(position).isBCorrect = false
        alAttendExam.get(position).isCCorrect = false
        if (alAttendExam.get(position).isDCorrect) {
            alAttendExam.get(position).isDCorrect = false
            inputParam.addProperty(Constant.REQUEST_OPTION_D_VALUE, "0")
        } else {
            alAttendExam.get(position).isDCorrect = true
            inputParam.addProperty(Constant.REQUEST_OPTION_D_VALUE, "1")
        }
        adapter?.notifyDataSetChanged()

        inputParam.addProperty(Constant.REQUEST_MODE, Constant.REQUEST_UPDATE_EXAM_ANSWERS)
        inputParam.addProperty(Constant.REUQEST_USER_ID, prefUtils.getUserData()?.userid)
        inputParam.addProperty(Constant.REQUEST_STUDENTID, prefUtils.getUserData()?.studentId)
        inputParam.addProperty(Constant.REQUEST_SR_NO, alAttendExam.get(position).srNo)
        inputParam.addProperty(Constant.REQUEST_OPTION_A_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_OPTION_B_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_OPTION_C_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_SUBJECTIVE_ANSWER, "")
        viewModel.executeUpdateExamAnswer(inputParam)
    }

    fun updateEditeTextAnswer(position: Int, srNumber: String, subAnswer: String) {
        LogM.e("=> subAnswer " + subAnswer)
        Coroutines.main {
            if (!oldAnswer.trim().equals(subAnswer.trim())) {
                val inputParam = JsonObject()
                if (subAnswer.equals("", ignoreCase = true)) {
                    alAttendExam.get(position).subjectiveanswer = ""
                    inputParam.addProperty(Constant.REQUEST_SUBJECTIVE_ANSWER, "")
                } else {
                    alAttendExam.get(position).subjectiveanswer = subAnswer
                    inputParam.addProperty(Constant.REQUEST_SUBJECTIVE_ANSWER, subAnswer)
                }
                inputParam.addProperty(Constant.REQUEST_MODE, Constant.REQUEST_UPDATE_EXAM_ANSWERS)
                inputParam.addProperty(Constant.REUQEST_USER_ID, prefUtils.getUserData()?.userid)
                inputParam.addProperty(
                    Constant.REQUEST_STUDENTID,
                    prefUtils.getUserData()?.studentId
                )
                inputParam.addProperty(Constant.REQUEST_SR_NO, srNumber)
                inputParam.addProperty(Constant.REQUEST_OPTION_A_VALUE, "0")
                inputParam.addProperty(Constant.REQUEST_OPTION_B_VALUE, "0")
                inputParam.addProperty(Constant.REQUEST_OPTION_C_VALUE, "0")
                inputParam.addProperty(Constant.REQUEST_OPTION_D_VALUE, "0")
                viewModel.executeUpdateExamAnswer(inputParam)
                oldAnswer = subAnswer
            }
        }
    }

    fun submitButtonClick() {
        if (isFromViewResult) {
            closeScreen()
        } else {
            AlertDialogUtility.CustomAlert(
                this@AttendExamActivity,
                getString(R.string.submit_exam_title),
                getString(R.string.submit_exam_message),
                "Yes",
                "No",
                { dialog, which ->
                    dialog.dismiss()
                    executeEndExamAPI()
                },
                { dialog, which ->
                    dialog.dismiss()
                })
        }
    }

    fun executeEndExamAPI() {
        viewModel.executeEndExam()
    }

    fun closeScreen() {
        UserProfileListner.getInstance().changeState(true)
        finish()
    }

    override fun onBackPressed() {
        /*We will allow backpress only is user is from View result.
        If user will from Attent exam then user should submit current question so that back press is stoped
         */
        if (isFromViewResult) {
            closeScreen()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.resetAllTimer()
    }

    fun openFile(strQuestionId: String, position: Int) {
        this.position = position
        this.strQuestionId = strQuestionId
        checkFileSubmitPermission()
    }

    fun checkFileSubmitPermission() =
        runWithPermissions(Manifest.permission.READ_EXTERNAL_STORAGE) {
            AlertDialogUtility.CustomAlert(this@AttendExamActivity,
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
                if(data?.data != null) {
                    val filePath = ImageFilePath.getPath(this@AttendExamActivity, data?.data)
                    val file: File = File(filePath)
                    uploadFile(file)
                }
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

    private fun uploadFile(file: File) {
        val fileSizeInBytes = file.length()
        val fileSizeInKB = fileSizeInBytes / 1024
        val fileSizeInMB = fileSizeInKB / 1024
        val allowedFileSize = prefUtils.getUserData()?.maxfileuploadsizekb!! / 1024
        if(fileSizeInMB >= allowedFileSize) {
            toast("Your selected file size is $fileSizeInMB MB")
            AlertDialogUtility.showSingleAlert(
                this@AttendExamActivity, getString(R.string.file_size_alert) + " \n" +
                        getString(R.string.max_file_size_allow) + " " +
                        allowedFileSize + " "
                        + getString(R.string.mb)
            ) { dialog, which ->
                dialog.dismiss()
            }
        } else {
            toast(resources.getString(R.string.file_uploading_starting))
            viewModel.uploadAnswerFile(strExamId, strQuestionId, file)
        }
    }

    //Multiple Image selection START
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

    private fun startCrop(uri: Uri) {
        val options = UCrop.Options()
        options.setFreeStyleCropEnabled(true)
        options.setHideBottomControls(true)

        val destinationFileName = ".jpg"
        val uCrop = UCrop.of(uri, Uri.fromFile(File(cacheDir, destinationFileName)))
        uCrop.withOptions(options)
        uCrop.start(this@AttendExamActivity)
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
            if(selected_image == alMultiImage.size + 1) {
                uploadFile(file!!)
            }
        } else {
            toast(getString(R.string.try_again_crop))
        }
    }

    private fun setFourthImage(resultUri: Uri) {
        selected_image = 5
        alMultiImage.get(3).imageUri = resultUri
        alMultiImage.get(3).isSelected = true
        if (alMultiImage.size > 4) {
            startCrop(alMultiImage.get(4).imageUri)
        }
    }

    private fun setThirdImage(resultUri: Uri) {
        selected_image = 4
        alMultiImage.get(2).imageUri = resultUri
        alMultiImage.get(2).isSelected = true
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
        val bitmap = BitmapFactory.decodeFile(alMultiImage.get(position).imageUri.path)
        val pageInfo =
            PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, position + 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()
        paint.setColor(Color.BLUE)
        canvas.drawPaint(paint)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        pdfDocument.finishPage(page)
        bitmap.recycle()
        pdfDocument.writeTo(fileOutputStream)
        LogM.e("PDF path is " + file?.absolutePath)
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