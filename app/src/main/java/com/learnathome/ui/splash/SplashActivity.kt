package com.learnathome.ui.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import com.learnathome.R
import com.learnathome.base.BaseActivity
import com.learnathome.utils.LogM

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

        LogM.e("=> User id checking ??? " + prefUtils.getUserId())

        Handler(Looper.getMainLooper()).postDelayed({
            if (prefUtils.getUserId().isNullOrEmpty()) {
                navigationController.navigateToLoginScreen(this@SplashActivity)
            } else {
                navigationController.navigateToHomeScreen(this@SplashActivity)
            }
            finish()
        }, 100)
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