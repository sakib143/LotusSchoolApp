package com.appforschool.utils

import android.content.Context
import com.appforschool.base.BaseActivity
import com.appforschool.ui.addtodrive.AddToDriveActivity
import com.appforschool.ui.attendexam.AttendExamActivity
import com.appforschool.ui.auth.changepassword.ChangePasswordActivity
import com.appforschool.ui.auth.login.LoginActivity
import com.appforschool.ui.commonwebview.CommonWebviewActivity
import com.appforschool.ui.home.HomeActivity
import com.appforschool.ui.profile.user.UserProfileActivity
import com.appforschool.ui.profile.teacher.TeacherProfileActivity
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
    fun navigateToVideoCallScreen(activity: BaseActivity, roomUrl: String,scheduleId: Int) {
        activity.startActivity(VideoCallingActivity.intentFor(context,roomUrl,scheduleId))
    }

    /**
    //     * Opens CommonWebview Screen
    //     * @param activity -> to Start the Activity
    //     */
    fun navigateToCommonWebviewScreen(activity: BaseActivity, url: String) {
        activity.startActivity(CommonWebviewActivity.intentFor(context, url))
    }

    /**
    //     * Opens Add to drive Screen
    //     * @param activity -> to Start the Activity
    //     */
    fun navigateToAddToDrive(activity: BaseActivity) {
        activity.startActivity(AddToDriveActivity.intentFor(context))
    }

    /**
    //     * Opens Change password screen
    //     * @param activity -> to Start the Activity
    //     */
    fun navigateToChangePassword(activity: BaseActivity) {
        activity.startActivity(ChangePasswordActivity.intentFor(context))
    }

    /**
    //     * Opens Teacher Profile screen
    //     * @param activity -> to Start the Activity
    //     */
    fun navigateToTeacherProfile(activity: BaseActivity) {
        activity.startActivity(TeacherProfileActivity.intentFor(context))
    }

    /**
    //     * Opens Student Profile screen
    //     * @param activity -> to Start the Activity
    //     */
    fun navigateToStudentProfile(activity: BaseActivity) {
        activity.startActivity(UserProfileActivity.intentFor(context))
    }

    /**
    //     * Opens Attend Exam screen
    //     * @param activity -> to Start the Activity
    //     */
    fun navigateToAttendExam(activity: BaseActivity, examId: String) {
        activity.startActivity(AttendExamActivity.intentFor(context,examId))
    }

}