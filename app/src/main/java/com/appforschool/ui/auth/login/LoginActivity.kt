package com.appforschool.ui.auth.login

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import com.appforschool.R
import com.appforschool.base.BaseBindingActivity
import com.appforschool.data.model.GetVersionModel
import com.appforschool.data.model.LoginModel
import com.appforschool.databinding.ActivityLoginBinding
import com.appforschool.utils.AlertDialogUtility
import com.appforschool.utils.toast
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
        viewModel.getLatestVersionName.observe(this, latestVersionObserver)
    }

    override fun onResume() {
        super.onResume()
        if (globalMethods.isInternetAvailable(this@LoginActivity)) {
            viewModel.executeLatestVersion()
        }
    }

    private val onMessageErrorObserver = Observer<Any> {
        toast(it.toString())
    }

    private val loginObserver = Observer<LoginModel> {
        if (it.status) {
            prefUtils.saveUserId(it.data.get(0).userid, it.data.get(0))
            navigationController.navigateToHomeScreen(this@LoginActivity)
            toast(it!!.message)
        } else {
            toast(it!!.message)
        }
    }

    private val latestVersionObserver = Observer<GetVersionModel> {
        if (it.status) {
            val latestVersion = it.data.get(0).currentVersion
            val currentVersion = globalMethods.getAppVersion(this@LoginActivity)
            val isForceUpdate = it.data.get(0).isForceUpdate

            if ( ! latestVersion.equals(currentVersion, ignoreCase = true) && isForceUpdate.equals("yes", ignoreCase = true)) {
                AlertDialogUtility.showSingleAlert(
                    this@LoginActivity, "Please update latest version."
                ) { dialog, which ->
                    dialog.dismiss()
                    try {
                        val appStoreIntent =
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=${this.packageName}")
                            )
                        appStoreIntent.setPackage("com.android.vending")
                        startActivity(appStoreIntent)
                    } catch (exception: ActivityNotFoundException) {
                        startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=$this.packageName")
                            )
                        )
                    }
                }
            }
        } else {
            toast(it!!.message)
        }
    }

}