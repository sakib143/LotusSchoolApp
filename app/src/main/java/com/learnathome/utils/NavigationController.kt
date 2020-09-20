package com.learnathome.utils

import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.learnathome.base.BaseActivity
import com.learnathome.ui.auth.login.LoginActivity
import com.learnathome.ui.home.HomeActivity
import com.learnathome.ui.videocalling.VideoCallingActivity
import javax.inject.Inject

/**
 * Class That Handles all Navigation between Activities
 */
class NavigationController @Inject constructor(var context: Context) {

//    /**
//    //     * Opens SignUp
//    //     * @param activity -> to Start the Activity
//    //     */
//    fun navigateToSignUpScreen(activity: BaseActivity) {
//        activity.startActivity(HelpScreenActivity.intentFor(context))
//    }
//
    /**
    //     * Opens Home screen
    //     * @param activity -> to Start the Activity
    //     */
    fun navigateToHomeScreen(activity: BaseActivity) {
        activity.startActivity(HomeActivity.intentFor(context))
    }

    /**
    //     * Opens Login  screen
    //     * @param activity -> to Start the Activity
    //     */
    fun navigateToLoginScreen(activity: BaseActivity) {
        activity.startActivity(LoginActivity.intentFor(context))
    }


    /**
    //     * Opens Video calling
    //     * @param activity -> to Start the Activity
    //     */
    fun navigateToVideoCallScreen(activity: BaseActivity, roomUrl: String) {
        activity.startActivity(VideoCallingActivity.intentFor(context,roomUrl))
    }


}