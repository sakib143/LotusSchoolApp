package com.appforschool.ui.auth.login

import android.app.AlertDialog
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

    @Inject
    lateinit var viewModel: LoginViewModel
    private var builder:AlertDialog.Builder? = null

    override fun layoutId() = R.layout.activity_login

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
        builder = AlertDialog.Builder(this@LoginActivity)
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
            val currentVersion = globalMethods.getAppVersion(this@LoginActivity)
            val latestVersion = it.data.get(0).currentVersion
            val isForceUpdate =  it.data.get(0).isForceUpdate
            if ( ! latestVersion.equals(currentVersion, ignoreCase = true) && isForceUpdate.equals(
                    "yes",
                    ignoreCase = true
                )) {
                setForceUpdatedialob()
            } else  if ( ! latestVersion.equals(currentVersion, ignoreCase = true) && isForceUpdate.equals(
                    "No",
                    ignoreCase = true
                )){
                if( ! prefUtils.isUpdateDialogVisible()) {
                    prefUtils.setUpdateAppDialog(true)
                    informUpdateDialog()
                }
            }
        } else {
            toast(it!!.message)
        }
    }

    private fun setForceUpdatedialob() {
        builder?.setTitle(R.string.app_name)
        builder?.setMessage(getString(R.string.update_force_update))
        builder?.setPositiveButton(getString(R.string.update)){ dialogInterface, which ->
            dialogInterface.dismiss()
            val appStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${this@LoginActivity.packageName}"))
            appStoreIntent.setPackage("com.android.vending")
            startActivity(appStoreIntent)
        }
        val alertDialog: AlertDialog = builder!!.create()
        alertDialog.setCancelable(false)
        if( ! alertDialog.isShowing) {
            alertDialog.show()
        }
    }

    private fun informUpdateDialog() {
        builder?.setTitle(R.string.app_name)
        builder?.setMessage(getString(R.string.update_force_update))
        builder?.setPositiveButton(getString(R.string.yes)){ dialogInterface, which ->
            dialogInterface.dismiss()
            val appStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=${this@LoginActivity.packageName}"))
            appStoreIntent.setPackage("com.android.vending")
            startActivity(appStoreIntent)
        }
        builder?.setNegativeButton(getString(R.string.no)){ dialogInterface, which ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder!!.create()
        alertDialog.setCancelable(true)
        if( ! alertDialog.isShowing) {
            alertDialog.show()
        }
    }

}