package com.appforschool.ui.attendexam

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.appforschool.R
import com.appforschool.base.BaseBindingActivity
import com.appforschool.data.model.AttendExamModel
import com.appforschool.data.model.ExamModel
import com.appforschool.data.model.UpdateExamAnswerModel
import com.appforschool.databinding.ActivityAttendExamBinding
import com.appforschool.utils.*
import kotlinx.android.synthetic.main.activity_attend_exam.*
import kotlinx.android.synthetic.main.fragment_subjects.*
import kotlinx.coroutines.CoroutineScope
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class AttendExamActivity : BaseBindingActivity<ActivityAttendExamBinding>() {

    private var duration: Int = 0
    private var formatedTime: String = ""

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

        duration = intent.getStringExtra(Constant.KEY_DURATION)!!.toInt()!!
        formatedTime = intent.getStringExtra(Constant.KEY_FORMATED_TIME)!!

        LogM.e("Durationd is$duration + time is $formatedTime")

        viewModel.setData(
            intent.getStringExtra(Constant.REQUEST_EXAM_ID)!!,
            intent.getStringExtra(Constant.REUQEST_GET_SUBJECTS)!!,
            intent.getStringExtra(Constant.KEY_DURATION)!!,
            intent.getStringExtra(Constant.KEY_MAKRS)!!,
            intent.getStringExtra(Constant.REQUEST_GET_EXAMS)!!,
            intent.getStringExtra(Constant.KEY_TIME)!!,
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
        viewModel.update_answer.observe(this, updateAnswer)
        viewModel.executeAttentExamList()

        setFirstAlert()
        setSecondAlert()
        setTimeOver()
    }

    private fun setTimeOver() {
        try {
            val dateFormat = SimpleDateFormat(Constant.DATE_FORMAT)
            val date = dateFormat.parse(formatedTime)
            val calendar = Calendar.getInstance()
            calendar!!.time = date
            calendar!!.add(Calendar.MINUTE, duration)
            val latestDate = calendar.time
            val timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    Handler(Looper.getMainLooper()).postDelayed({
                        closeScreen()
                    }, 5000)
                }
            }, latestDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    private fun setFirstAlert() {
        try {
            val dateFormat = SimpleDateFormat(Constant.DATE_FORMAT)
            val date = dateFormat.parse(formatedTime)
            val calendar = Calendar.getInstance()
            calendar!!.time = date
            calendar!!.add(Calendar.MINUTE, duration - 5)
            val latestDate = calendar.time
            val timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    Handler(Looper.getMainLooper()).postDelayed({
                        setFiveMinRemainAlert()
                    }, 1000)
                }
            }, latestDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    private fun setSecondAlert() {
        try {
            val dateFormat = SimpleDateFormat(Constant.DATE_FORMAT)
            val date = dateFormat.parse(formatedTime)
            val calendar = Calendar.getInstance()
            calendar!!.time = date
            calendar!!.add(Calendar.MINUTE, duration - 1)
            val latestDate = calendar.time
            val timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    Handler(Looper.getMainLooper()).postDelayed({
                        toast(resources.getString(R.string.exam_min_left_second_message))
                    }, 1000)
                }
            }, latestDate)
        } catch (e: ParseException) {
            e.printStackTrace()
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


    private val updateAnswer = Observer<UpdateExamAnswerModel> {
        if (it.status) {
            toast(it!!.message)
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
        viewModel.executeUpdateExamAnswer(srNumber, alAttendExam.get(position).optionA, "")
    }

    fun optionBClicked(position: Int, srNumber: String) {
        alAttendExam.get(position).isACorrect = false
        alAttendExam.get(position).isBCorrect = true
        alAttendExam.get(position).isCCorrect = false
        alAttendExam.get(position).isDCorrect = false
        adapter?.notifyDataSetChanged()
        viewModel.executeUpdateExamAnswer(srNumber, alAttendExam.get(position).optionB, "")
    }

    fun optionCClicked(position: Int, srNumber: String) {
        alAttendExam.get(position).isACorrect = false
        alAttendExam.get(position).isBCorrect = false
        alAttendExam.get(position).isCCorrect = true
        alAttendExam.get(position).isDCorrect = false
        adapter?.notifyDataSetChanged()
        viewModel.executeUpdateExamAnswer(srNumber, alAttendExam.get(position).optionC, "")
    }

    fun optionDClicked(position: Int, srNumber: String) {
        alAttendExam.get(position).isACorrect = false
        alAttendExam.get(position).isBCorrect = false
        alAttendExam.get(position).isCCorrect = false
        alAttendExam.get(position).isDCorrect = true
        adapter?.notifyDataSetChanged()
        viewModel.executeUpdateExamAnswer(srNumber, alAttendExam.get(position).optionD, "")
    }

    fun updateEditeTextAnswer(srNumber: String, subAnswer: String) {
        viewModel.executeUpdateExamAnswer(srNumber, "", subAnswer)
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
                closeScreen()
            },
            { dialog, which ->
                dialog.dismiss()
            })
    }

    fun closeScreen() {
        finish()
    }

}