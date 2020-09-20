package com.learnathome.ui.auth.login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import com.learnathome.R
import com.learnathome.base.BaseBindingActivity
import com.learnathome.data.model.LoginModel
import com.learnathome.databinding.ActivityLoginBinding
import com.learnathome.utils.toast
import javax.inject.Inject

class LoginActivity : BaseBindingActivity<ActivityLoginBinding>() {

    override fun layoutId() = R.layout.activity_login

    @Inject
    lateinit var viewModel: LoginViewModel

    override fun initializeBinding(binding: ActivityLoginBinding) {
        binding.loginViewModel = viewModel
        binding.lifecycleOwner = this
        binding.listner = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setData()

    }

    companion object {
        @JvmStatic
        fun intentFor(context: Context) =
            Intent(
                context,
                LoginActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    }

    private fun setData() {
        viewModel.onMessageError.observe(this, onMessageErrorObserver)
        viewModel.login_data.observe(this, loginObserver)
    }


    private val onMessageErrorObserver = Observer<Any> {
        toast(it.toString())
    }

    private val loginObserver = Observer<LoginModel> {
        if (it.status) {
            prefUtils.saveUserId(it.data.get(0).studentId,it.data.get(0).studentname)
            navigationController.navigateToHomeScreen(this@LoginActivity)
            toast(it!!.message)
        } else {
            toast(it!!.message)
        }
    }


}