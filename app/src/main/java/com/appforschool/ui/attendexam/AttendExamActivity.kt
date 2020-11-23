package com.appforschool.ui.attendexam

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.appforschool.R
import com.appforschool.base.BaseBindingActivity
import com.appforschool.data.model.AttendExamModel
import com.appforschool.databinding.ActivityAttendExamBinding
import com.appforschool.utils.Constant
import com.appforschool.utils.toast
import javax.inject.Inject

class AttendExamActivity  : BaseBindingActivity<ActivityAttendExamBinding>() {

    override fun layoutId() = R.layout.activity_attend_exam

    @Inject
    lateinit var viewModel: AttendExamViewModel

    override fun initializeBinding(binding: ActivityAttendExamBinding) {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.listner = this
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
            toast(it!!.message)
        } else {
            toast(it!!.message)
        }
    }


}