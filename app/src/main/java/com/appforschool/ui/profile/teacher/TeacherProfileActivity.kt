package com.appforschool.ui.profile.teacher

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.appforschool.R
import com.appforschool.base.BaseBindingActivity
import com.appforschool.databinding.ActivityStudentProfileBinding
import com.appforschool.databinding.ActivityTeacherProfileBinding
import com.appforschool.ui.auth.changepassword.ChangePasswordActivity

class TeacherProfileActivity :  BaseBindingActivity<ActivityTeacherProfileBinding>() {

    override fun layoutId() = R.layout.activity_teacher_profile

    override fun initializeBinding(binding: ActivityTeacherProfileBinding) {
        binding.lifecycleOwner = this
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

}