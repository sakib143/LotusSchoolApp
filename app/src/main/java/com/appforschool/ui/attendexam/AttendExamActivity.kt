package com.appforschool.ui.attendexam

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.appforschool.R
import com.appforschool.base.BaseBindingActivity
import com.appforschool.data.model.AttendExamModel
import com.appforschool.data.model.EndExamModel
import com.appforschool.databinding.ActivityAttendExamBinding
import com.appforschool.listner.UserProfileListner
import com.appforschool.utils.*
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_attend_exam.*
import kotlinx.android.synthetic.main.fragment_subjects.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class AttendExamActivity : BaseBindingActivity<ActivityAttendExamBinding>()  {


    override fun layoutId() = R.layout.activity_attend_exam

    @Inject
    lateinit var viewModel: AttendExamViewModel

    private var alAttendExam: ArrayList<AttendExamModel.Data> = ArrayList()
    private var adapter: AttendExamAdapter? = null
    private var binding: ActivityAttendExamBinding? = null

    override fun initializeBinding(binding: ActivityAttendExamBinding) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.listner = this
        this.binding = binding
        binding.alAttendExam = alAttendExam
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.setData(
            intent.getStringExtra(Constant.REQUEST_EXAM_ID)!!,
            intent.getStringExtra(Constant.REQUEST_GET_EXAMS)!!,
            intent.getStringExtra(Constant.REUQEST_GET_SUBJECTS)!!,
            intent.getStringExtra(Constant.KEY_MAKRS)!!,
            intent.getStringExtra(Constant.KEY_DURATION)!!,
            intent.getStringExtra(Constant.KEY_TIME)!!,
            intent.getStringExtra(Constant.KEY_FORMATED_TIME)!!)

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
        viewModel.endExam.observe(this,endExamObserver)

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

    private val endExamObserver = Observer<EndExamModel> {
        if (it.status) {
            toast(it!!.data.get(0).message)
            closeScreen()
        } else {
            toast(it!!.message)
        }
    }

    fun optionAClicked(position: Int, srNumber: String) {
        alAttendExam.get(position).isACorrect = true
        alAttendExam.get(position).isBCorrect = false
        alAttendExam.get(position).isCCorrect = false
        alAttendExam.get(position).isDCorrect = false
        adapter?.notifyDataSetChanged()

        val inputParam = JsonObject()
        inputParam.addProperty(Constant.REQUEST_MODE, Constant.REQUEST_UPDATE_EXAM_ANSWERS)
        inputParam.addProperty(Constant.REUQEST_USER_ID, prefUtils.getUserData()?.userid)
        inputParam.addProperty(Constant.REQUEST_STUDENTID, prefUtils.getUserData()?.studentId)
        inputParam.addProperty(Constant.REQUEST_SR_NO, alAttendExam.get(position).srNo)
        inputParam.addProperty(Constant.REQUEST_OPTION_A_VALUE, "1")
        inputParam.addProperty(Constant.REQUEST_OPTION_B_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_OPTION_C_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_OPTION_D_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_SUBJECTIVE_ANSWER, "")
        viewModel.executeUpdateExamAnswer(inputParam)
    }

    fun optionBClicked(position: Int, srNumber: String) {
        alAttendExam.get(position).isACorrect = false
        alAttendExam.get(position).isBCorrect = true
        alAttendExam.get(position).isCCorrect = false
        alAttendExam.get(position).isDCorrect = false
        adapter?.notifyDataSetChanged()

        val inputParam = JsonObject()
        inputParam.addProperty(Constant.REQUEST_MODE, Constant.REQUEST_UPDATE_EXAM_ANSWERS)
        inputParam.addProperty(Constant.REUQEST_USER_ID, prefUtils.getUserData()?.userid)
        inputParam.addProperty(Constant.REQUEST_STUDENTID, prefUtils.getUserData()?.studentId)
        inputParam.addProperty(Constant.REQUEST_SR_NO, alAttendExam.get(position).srNo)
        inputParam.addProperty(Constant.REQUEST_OPTION_A_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_OPTION_B_VALUE, "1")
        inputParam.addProperty(Constant.REQUEST_OPTION_C_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_OPTION_D_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_SUBJECTIVE_ANSWER, "")
        viewModel.executeUpdateExamAnswer(inputParam)
    }

    fun optionCClicked(position: Int, srNumber: String) {
        alAttendExam.get(position).isACorrect = false
        alAttendExam.get(position).isBCorrect = false
        alAttendExam.get(position).isCCorrect = true
        alAttendExam.get(position).isDCorrect = false
        adapter?.notifyDataSetChanged()

        val inputParam = JsonObject()
        inputParam.addProperty(Constant.REQUEST_MODE, Constant.REQUEST_UPDATE_EXAM_ANSWERS)
        inputParam.addProperty(Constant.REUQEST_USER_ID, prefUtils.getUserData()?.userid)
        inputParam.addProperty(Constant.REQUEST_STUDENTID, prefUtils.getUserData()?.studentId)
        inputParam.addProperty(Constant.REQUEST_SR_NO, alAttendExam.get(position).srNo)
        inputParam.addProperty(Constant.REQUEST_OPTION_A_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_OPTION_B_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_OPTION_C_VALUE, "1")
        inputParam.addProperty(Constant.REQUEST_OPTION_D_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_SUBJECTIVE_ANSWER, "")
        viewModel.executeUpdateExamAnswer(inputParam)
    }

    fun optionDClicked(position: Int, srNumber: String) {
        alAttendExam.get(position).isACorrect = false
        alAttendExam.get(position).isBCorrect = false
        alAttendExam.get(position).isCCorrect = false
        alAttendExam.get(position).isDCorrect = true
        adapter?.notifyDataSetChanged()

        val inputParam = JsonObject()
        inputParam.addProperty(Constant.REQUEST_MODE, Constant.REQUEST_UPDATE_EXAM_ANSWERS)
        inputParam.addProperty(Constant.REUQEST_USER_ID, prefUtils.getUserData()?.userid)
        inputParam.addProperty(Constant.REQUEST_STUDENTID, prefUtils.getUserData()?.studentId)
        inputParam.addProperty(Constant.REQUEST_SR_NO, alAttendExam.get(position).srNo)
        inputParam.addProperty(Constant.REQUEST_OPTION_A_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_OPTION_B_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_OPTION_C_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_OPTION_D_VALUE, "1")
        inputParam.addProperty(Constant.REQUEST_SUBJECTIVE_ANSWER, "")
        viewModel.executeUpdateExamAnswer(inputParam)
    }

    fun updateEditeTextAnswer(srNumber: String, subAnswer: String) {
        val inputParam = JsonObject()
        inputParam.addProperty(Constant.REQUEST_MODE, Constant.REQUEST_UPDATE_EXAM_ANSWERS)
        inputParam.addProperty(Constant.REUQEST_USER_ID, prefUtils.getUserData()?.userid)
        inputParam.addProperty(Constant.REQUEST_STUDENTID, prefUtils.getUserData()?.studentId)
        inputParam.addProperty(Constant.REQUEST_SR_NO, srNumber)
        inputParam.addProperty(Constant.REQUEST_OPTION_A_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_OPTION_B_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_OPTION_C_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_OPTION_D_VALUE, "0")
        inputParam.addProperty(Constant.REQUEST_SUBJECTIVE_ANSWER, subAnswer)
        viewModel.executeUpdateExamAnswer(inputParam)
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
}