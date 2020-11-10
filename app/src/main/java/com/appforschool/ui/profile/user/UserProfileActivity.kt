package com.appforschool.ui.profile.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.appforschool.R
import com.appforschool.base.BaseBindingActivity
import com.appforschool.data.model.LoginModel
import com.appforschool.data.model.UpdateProfileModel
import com.appforschool.databinding.ActivityUserProfileBinding
import com.appforschool.utils.toast
import javax.inject.Inject

class UserProfileActivity :  BaseBindingActivity<ActivityUserProfileBinding>() {

    @Inject
    lateinit var viewModel: UserProfileViewModel

    override fun layoutId() = R.layout.activity_user_profile

    override fun initializeBinding(binding: ActivityUserProfileBinding) {
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        binding.listner = this
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setObserver()
    }

    companion object {
        @JvmStatic
        fun intentFor(
            context: Context
        ) = Intent(context, UserProfileActivity::class.java)
    }

    private fun setObserver() {
        viewModel.onMessageError.observe(this, onMessageErrorObserver)
        viewModel.update_profile.observe(this, updateProfileObserver)
    }

    private val updateProfileObserver = Observer<UpdateProfileModel> {
        if (it.status) {
            toast(it!!.message)
        } else {
            toast(it!!.message)
        }
    }

    private val onMessageErrorObserver = Observer<Any> {
        toast(it.toString())
    }

    fun closeScreen() {
        finish()
    }
}