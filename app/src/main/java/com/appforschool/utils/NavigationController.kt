package com.appforschool.utils

import android.content.Context
import com.appforschool.base.BaseActivity
import com.appforschool.ui.auth.login.LoginActivity
import com.appforschool.ui.commonwebview.CommonWebviewActivity
import com.appforschool.ui.home.HomeActivity
import com.appforschool.ui.videocalling.VideoCallingActivity
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
    fun navigateToVideoCallScreen(activity: BaseActivity, roomUrl: String, userName: String,ishost: Int,scheduleId: Int) {
        activity.startActivity(VideoCallingActivity.intentFor(context,roomUrl,userName,ishost,scheduleId))
    }

    /**
    //     * Opens CommonWebview Screen
    //     * @param activity -> to Start the Activity
    //     */
    fun navigateToCommonWebviewScreen(activity: BaseActivity, url: String) {
        activity.startActivity(CommonWebviewActivity.intentFor(context, url))
    }



}