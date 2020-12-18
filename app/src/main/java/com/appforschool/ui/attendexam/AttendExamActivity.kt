package com.appforschool.ui.attendexam

import android.Manifest
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.appforschool.R
import com.appforschool.base.BaseBindingActivity
import com.appforschool.data.model.AssignmentModel
import com.appforschool.data.model.AttendExamModel
import com.appforschool.data.model.EndExamModel
import com.appforschool.data.model.UploadAnswerFileModel
import com.appforschool.databinding.ActivityAttendExamBinding
import com.appforschool.listner.UserProfileListner
import com.appforschool.ui.full_image.FullImageActivity
import com.appforschool.utils.*
import com.google.gson.JsonObject
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import kotlinx.android.synthetic.main.activity_attend_exam.*
import kotlinx.android.synthetic.main.fragment_subjects.*
import java.io.File
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

    override fun initializeBinding(binding: ActivityAttendExamBinding) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.listner = this
        this.binding = binding
        binding.alAttendExam = alAttendExam
    }

    private var strExamId: String? =  null
    private var strQuestionId: String? = null
    private var position: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        strExamId = intent.getStringExtra(Constant.REQUEST_EXAM_ID)!!

        viewModel.setData(
            intent.getStringExtra(Constant.REQUEST_EXAM_ID)!!,
            intent.getStringExtra(Constant.REQUEST_GET_EXAMS)!!,
            intent.getStringExtra(Constant.REUQEST_GET_SUBJECTS)!!,
            intent.getStringExtra(Constant.KEY_MAKRS)!!,
            intent.getStringExtra(Constant.KEY_DURATION)!!,
            intent.getStringExtra(Constant.KEY_TIME)!!,
            intent.getStringExtra(Constant.KEY_FORMATED_TIME)!!
        )
        setData()
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
            formatedTime: String
        ) =
            Intent(context, AttendExamActivity::class.java)
                .putExtra(Constant.REQUEST_EXAM_ID, examId)
                .putExtra(Constant.REQUEST_GET_EXAMS, examName)
                .putExtra(Constant.REUQEST_GET_SUBJECTS, subject)
                .putExtra(Constant.KEY_MAKRS, makrs)
                .putExtra(Constant.KEY_DURATION, duration)
                .putExtra(Constant.KEY_TIME, time)
                .putExtra(Constant.KEY_FORMATED_TIME, formatedTime)
    }

    private fun setData() {
        viewModel.onMessageError.observe(this, onMessageErrorObserver)
        viewModel.attend_exam.observe(this, attendExamObserver)
        viewModel.setFiveMinuteLeft.observe(this, onFiveMinuteLeft)
        viewModel.setTimeOver.observe(this, onTimeOverObserver)
        viewModel.endExam.observe(this, endExamObserver)
        viewModel.uploadAnswer.observe(this,uploadAnswerFileObserver)

        viewModel.executeAttentExamList() //API call to fetch list
        viewModel.fiveMinuteLeftAlert()
        viewModel.oneMinuteLeftAlert()
        viewModel.timeOverAlert()
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
                adapter = AttendExamAdapter(this@AttendExamActivity, it.data)
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

    fun zoomImage(imageUrl: String, image:ImageView) {
        ViewCompat.setTransitionName(image, Constant.IMAGE_FULL_ZOOM_ANIM)
        val intent = Intent(this, FullImageActivity::class.java)
        intent.putExtra(Constant.REQUEST_LINK_URL, imageUrl)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            image!!,
            ViewCompat.getTransitionName(image)!!)
        startActivity(intent, options.toBundle())
    }

    fun optionAClicked(position: Int, srNumber: String) {
        val inputParam = JsonObject()
        if(alAttendExam.get(position).isACorrect){
            alAttendExam.get(position).isACorrect = false
            inputParam.addProperty(Constant.REQUEST_OPTION_A_VALUE, "0")
        }else {
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
        if(alAttendExam.get(position).isBCorrect) {
            alAttendExam.get(position).isBCorrect = false
            inputParam.addProperty(Constant.REQUEST_OPTION_B_VALUE, "0")
        }else {
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
        if(alAttendExam.get(position).isCCorrect){
            alAttendExam.get(position).isCCorrect = false
            inputParam.addProperty(Constant.REQUEST_OPTION_C_VALUE, "0")
        }else {
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
            if( ! oldAnswer.trim().equals(subAnswer.trim())){
                val inputParam = JsonObject()
                if(subAnswer.equals("",ignoreCase = true)){
                    alAttendExam.get(position).subjectiveanswer = ""
                    inputParam.addProperty(Constant.REQUEST_SUBJECTIVE_ANSWER, "")
                }else {
                    alAttendExam.get(position).subjectiveanswer = subAnswer
                    inputParam.addProperty(Constant.REQUEST_SUBJECTIVE_ANSWER, subAnswer)
                }
                inputParam.addProperty(Constant.REQUEST_MODE, Constant.REQUEST_UPDATE_EXAM_ANSWERS)
                inputParam.addProperty(Constant.REUQEST_USER_ID, prefUtils.getUserData()?.userid)
                inputParam.addProperty(Constant.REQUEST_STUDENTID, prefUtils.getUserData()?.studentId)
                inputParam.addProperty(Constant.REQUEST_SR_NO, srNumber)
                inputParam.addProperty(Constant.REQUEST_OPTION_A_VALUE, "0")
                inputParam.addProperty(Constant.REQUEST_OPTION_B_VALUE, "0")
                inputParam.addProperty(Constant.REQUEST_OPTION_C_VALUE, "0")
                inputParam.addProperty(Constant.REQUEST_OPTION_D_VALUE, "0")
                viewModel.executeUpdateExamAnswer(inputParam)
                oldAnswer =  subAnswer
            }
        }
    }

    fun submitButtonClick() {
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

    fun executeEndExamAPI() {
        viewModel.executeEndExam()
    }

    fun closeScreen() {
        UserProfileListner.getInstance().changeState(true)
        finish()
    }

    override fun onBackPressed() {
//        closeScreen()
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

    fun checkFileSubmitPermission() = runWithPermissions(Manifest.permission.READ_EXTERNAL_STORAGE) {
            var chooseFile = Intent(Intent.ACTION_GET_CONTENT)
            chooseFile.setType("*/*")
            chooseFile = Intent.createChooser(chooseFile, "Choose a file")
            startActivityForResult(chooseFile, PICKFILE_RESULT_CODE)
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PICKFILE_RESULT_CODE -> {
                val filePath = ImageFilePath.getPath(this@AttendExamActivity, data?.data)
                val file: File = File(filePath)
                viewModel.uploadAnswerFile(strExamId,strQuestionId,file)
            }
        }
    }
}