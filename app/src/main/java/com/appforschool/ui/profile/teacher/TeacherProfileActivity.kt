package com.appforschool.ui.profile.teacher

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.appforschool.R
import com.appforschool.base.BaseBindingActivity
import com.appforschool.databinding.ActivityTeacherProfileBinding
import javax.inject.Inject

class TeacherProfileActivity :  BaseBindingActivity<ActivityTeacherProfileBinding>() {

    @Inject
    lateinit var viewModel: TeacherProfileViewModel
    override fun layoutId() = R.layout.activity_teacher_profile

    override fun initializeBinding(binding: ActivityTeacherProfileBinding) {
        binding.lifecycleOwner = this
        binding.listner = this
        binding.viewmodel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun intentFor(
            context: Context
        ) = Intent(context, TeacherProfileActivity::class.java)

    }

    fun closeScreen() {
        finish()
    }

}