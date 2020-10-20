package com.appforschool.ui.addtodrive

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.appforschool.R
import com.appforschool.base.BaseBindingActivity
import com.appforschool.databinding.ActivityAddToDriveBinding
import com.appforschool.databinding.ActivityLoginBinding
import com.appforschool.ui.auth.login.LoginViewModel
import com.appforschool.ui.videocalling.VideoCallingActivity
import com.appforschool.utils.Constant
import javax.inject.Inject

class AddToDriveActivity  : BaseBindingActivity<ActivityAddToDriveBinding>() {

    override fun layoutId() = R.layout.activity_add_to_drive

    @Inject
    lateinit var viewModel: AddToDriveViewModel

    override fun initializeBinding(binding: ActivityAddToDriveBinding) {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        binding.listner = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun intentFor(
            context: Context
        ) = Intent(context, AddToDriveActivity::class.java)

    }

    fun closeScreen(){
        finish()
    }
}