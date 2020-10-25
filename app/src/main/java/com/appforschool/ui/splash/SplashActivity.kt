package com.appforschool.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import com.appforschool.R
import com.appforschool.base.BaseActivity
import com.appforschool.utils.LogM

class SplashActivity : BaseActivity() {

    override fun layoutId() = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        setWindow()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        this.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        if (prefUtils.getUserId().isNullOrEmpty()) {
            navigationController.navigateToLoginScreen((this@SplashActivity))
        } else {
            navigationController.navigateToAddToDrive(this@SplashActivity)
        }
        finish()
    }

    private fun setWindow() {
        this.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

}