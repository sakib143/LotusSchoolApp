package com.appforschool.ui.attendexam

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.appforschool.R
import com.appforschool.base.BaseBindingActivity
import com.appforschool.data.model.AssignmentModel
import com.appforschool.data.model.AttendExamModel
import com.appforschool.data.model.ExamModel
import com.appforschool.databinding.ActivityAttendExamBinding
import com.appforschool.databinding.FragmentExamlistBinding
import com.appforschool.listner.AttendExamListner
import com.appforschool.utils.Constant
import com.appforschool.utils.toast
import javax.inject.Inject

class AttendExamActivity  : BaseBindingActivity<ActivityAttendExamBinding>() , AttendExamListner{

    override fun layoutId() = R.layout.activity_attend_exam

    @Inject
    lateinit var viewModel: AttendExamViewModel

    private var alAttendExam: ArrayList<AttendExamModel.Data> = ArrayList()
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

        viewModel.setData(intent.getStringExtra(Constant.REQUEST_EXAM_ID))
        setData()

    }

    companion object {
        @JvmStatic
        fun intentFor(context: Context, examId : String) =
            Intent(context, AttendExamActivity::class.java)
                .putExtra(Constant.REQUEST_EXAM_ID, examId)
    }

    private fun setData() {
        viewModel.onMessageError.observe(this, onMessageErrorObserver)
        viewModel.attend_exam.observe(this, attendExamObserver)
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
            }
        } else {
            toast(it!!.message)
            viewModel.setDataFound(true)
        }
    }

    override fun attendExamClick(model: AssignmentModel.Data) {

    }



}