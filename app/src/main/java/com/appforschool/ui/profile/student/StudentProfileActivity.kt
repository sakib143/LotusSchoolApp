package com.appforschool.ui.profile.student

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.appforschool.R
import com.appforschool.base.BaseBindingActivity
import com.appforschool.databinding.ActivityHomeBinding
import com.appforschool.databinding.ActivityStudentProfileBinding
import com.appforschool.ui.auth.changepassword.ChangePasswordViewModel
import com.appforschool.ui.profile.teacher.TeacherProfileActivity
import javax.inject.Inject

class StudentProfileActivity :  BaseBindingActivity<ActivityStudentProfileBinding>() {

    @Inject
    lateinit var viewModel: StudentProfileViewModel

    override fun layoutId() = R.layout.activity_student_profile

    override fun initializeBinding(binding: ActivityStudentProfileBinding) {
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        binding.listner = this
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun intentFor(
            context: Context
        ) = Intent(context, StudentProfileActivity::class.java)

    }

    fun closeScreen() {
        finish()
    }
}