package com.appforschool.ui.attendexam

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.appforschool.R
import com.appforschool.base.BaseBindingActivity
import com.appforschool.data.model.AssignmentModel
import com.appforschool.data.model.AttendExamModel
import com.appforschool.data.model.ExamModel
import com.appforschool.data.model.UpdateExamAnswerModel
import com.appforschool.databinding.ActivityAttendExamBinding
import com.appforschool.databinding.FragmentExamlistBinding
import com.appforschool.listner.AttendExamListner
import com.appforschool.ui.home.fragment.subject.SubjectAdapter
import com.appforschool.ui.home.fragment.subject.SubjectFragment
import com.appforschool.utils.Constant
import com.appforschool.utils.toast
import kotlinx.android.synthetic.main.activity_attend_exam.*
import kotlinx.android.synthetic.main.fragment_subjects.*
import javax.inject.Inject

class AttendExamActivity : BaseBindingActivity<ActivityAttendExamBinding>() {

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
            intent.getStringExtra(Constant.REUQEST_GET_SUBJECTS)!!,
            intent.getStringExtra(Constant.KEY_DURATION)!!,
            intent.getStringExtra(Constant.KEY_MAKRS)!!,
            intent.getStringExtra(Constant.REQUEST_GET_EXAMS)!!,
            intent.getStringExtra(Constant.KEY_TIME)!!
            )

        setData()

    }

    companion object {
        @JvmStatic
        fun intentFor(context: Context, examId: String,subject: String,duration: String,makrs: String,examName: String, time: String) =
            Intent(context, AttendExamActivity::class.java)
                .putExtra(Constant.REQUEST_EXAM_ID, examId)
                .putExtra(Constant.REQUEST_GET_EXAMS, examName)
                .putExtra(Constant.REUQEST_GET_SUBJECTS, subject)
                .putExtra(Constant.KEY_MAKRS, makrs)
                .putExtra(Constant.KEY_DURATION, duration)
                .putExtra(Constant.KEY_TIME, time)

    }

    private fun setData() {
        viewModel.onMessageError.observe(this, onMessageErrorObserver)
        viewModel.attend_exam.observe(this, attendExamObserver)
        viewModel.update_answer.observe(this,updateAnswer)
        viewModel.executeAttentExamList()
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


    fun optionAClicked(position: Int,srNumber: String) {
        alAttendExam.get(position).isACorrect = true
        alAttendExam.get(position).isBCorrect = false
        alAttendExam.get(position).isCCorrect = false
        alAttendExam.get(position).isDCorrect = false
        adapter?.notifyDataSetChanged()
        viewModel.executeUpdateExamAnswer(srNumber,alAttendExam.get(position).optionA,"")
    }

    fun optionBClicked(position: Int,srNumber: String) {
        alAttendExam.get(position).isACorrect = false
        alAttendExam.get(position).isBCorrect = true
        alAttendExam.get(position).isCCorrect = false
        alAttendExam.get(position).isDCorrect = false
        adapter?.notifyDataSetChanged()
        viewModel.executeUpdateExamAnswer(srNumber,alAttendExam.get(position).optionB,"")
    }

    fun optionCClicked(position: Int,srNumber: String) {
        alAttendExam.get(position).isACorrect = false
        alAttendExam.get(position).isBCorrect = false
        alAttendExam.get(position).isCCorrect = true
        alAttendExam.get(position).isDCorrect = false
        adapter?.notifyDataSetChanged()
        viewModel.executeUpdateExamAnswer(srNumber,alAttendExam.get(position).optionC,"")
    }

    fun optionDClicked(position: Int,srNumber: String) {
        alAttendExam.get(position).isACorrect = false
        alAttendExam.get(position).isBCorrect = false
        alAttendExam.get(position).isCCorrect = false
        alAttendExam.get(position).isDCorrect = true
        adapter?.notifyDataSetChanged()
        viewModel.executeUpdateExamAnswer(srNumber,alAttendExam.get(position).optionD,"")
    }

    fun updateEditeTextAnswer(srNumber: String, subAnswer: String) {
        viewModel.executeUpdateExamAnswer(srNumber,"",subAnswer)
    }

    fun closeScreen() {
        finish()
    }

}