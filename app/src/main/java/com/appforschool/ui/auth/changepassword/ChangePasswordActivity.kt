package com.appforschool.ui.auth.changepassword

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.appforschool.R
import com.appforschool.base.BaseBindingActivity
import com.appforschool.databinding.ActivityChangePasswordBinding
import javax.inject.Inject

class ChangePasswordActivity : BaseBindingActivity<ActivityChangePasswordBinding>() {

    @Inject
    lateinit var viewModel: ChangePasswordViewModel

    override fun layoutId() = R.layout.activity_change_password

    override fun initializeBinding(binding: ActivityChangePasswordBinding) {
        binding.lifecycleOwner = this
        binding.listner = this
        binding.changePasswordViewModel = viewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun intentFor(
            context: Context
        ) = Intent(context, ChangePasswordActivity::class.java)

    }

    fun closeScreen() {
        finish()
    }
}