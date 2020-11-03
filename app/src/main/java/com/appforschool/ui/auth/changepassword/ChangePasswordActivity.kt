package com.appforschool.ui.auth.changepassword

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.appforschool.R
import com.appforschool.base.BaseBindingActivity
import com.appforschool.data.model.ChangePasswordModel
import com.appforschool.data.model.LoginModel
import com.appforschool.databinding.ActivityChangePasswordBinding
import com.appforschool.utils.toast
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

        setObserver()
    }

    private fun setObserver() {
        viewModel.onMessageError.observe(this, onMessageErrorObserver)
        viewModel.change_password_api.observe(this,changePasswordObserver)
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

    private val onMessageErrorObserver = Observer<Any> {
        toast(it.toString())
    }

    private val changePasswordObserver = Observer<ChangePasswordModel> {
        if (it.status) {
            toast(it!!.message)
        } else {
            toast(it!!.message)
        }
    }


}