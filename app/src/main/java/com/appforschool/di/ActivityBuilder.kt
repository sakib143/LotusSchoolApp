package com.appforschool.di

import com.appforschool.ui.addtodrive.AddToDriveActivity
import com.appforschool.ui.attendexam.AttendExamActivity
import com.appforschool.ui.auth.changepassword.ChangePasswordActivity
import com.appforschool.ui.home.HomeActivity
import com.appforschool.ui.home.HomeBuilderModule
import com.appforschool.ui.auth.login.LoginActivity
import com.appforschool.ui.commonwebview.CommonWebviewActivity
import com.appforschool.ui.full_image.FullExamDateActivity
import com.appforschool.ui.full_image.FullImageActivity
import com.appforschool.ui.profile.user.UserProfileActivity
import com.appforschool.ui.profile.teacher.TeacherProfileActivity
import com.appforschool.ui.splash.SplashActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Helps to generate an {@link AndroidInjector} for all activities
 * */
@Suppress("unused")
@Module
abstract class ActivityBuilder {

    /**
     * fun to bind Splash Activity, making Injection enable
     **/
    @ContributesAndroidInjector()
    abstract fun bindSplashActivity() : SplashActivity


    /**
     * fun to bind Home screen , making Injection enable
     **/
    @ContributesAndroidInjector(modules = [(HomeBuilderModule::class)])
    abstract fun bindHomeNewActivity(): HomeActivity

    /**
     * fun to bind Login screen , making Injection enable
     **/
    @ContributesAndroidInjector()
    abstract fun bindLoginActivity(): LoginActivity

    /**
     * fun to bind Login screen , making Injection enable
     **/
    @ContributesAndroidInjector()
    abstract fun bindCommonWebviewActivity(): CommonWebviewActivity

    /**
     * fun to bind AddToDriveActivity screen , making Injection enable
     **/
    @ContributesAndroidInjector()
    abstract fun bindAddToDriveActivity(): AddToDriveActivity

    /**
     * fun to bind AddToDriveActivity screen , making Injection enable
     **/
    @ContributesAndroidInjector()
    abstract fun bindChangePasswordActivity(): ChangePasswordActivity


    /**
     * fun to bind Student Profile Activity, making Injection enable
     **/
    @ContributesAndroidInjector()
    abstract fun bindStudentProfileActivity() : UserProfileActivity

    /**
     * fun to bind Teacher Profile Activity, making Injection enable
     **/
    @ContributesAndroidInjector()
    abstract fun bindSTeacherProfileActivity() : TeacherProfileActivity

    /**
     * fun to bind Teacher Profile Activity, making Injection enable
     **/
    @ContributesAndroidInjector()
    abstract fun bindSAttendExamActivity() : AttendExamActivity

    /**
     * fun to bind Image full screen Activity, making Injection enable
     **/
    @ContributesAndroidInjector()
    abstract fun bindFullImageActivity() : FullImageActivity

    /**
     * fun to bind Full Exam Date time, making Injection enable
     **/
    @ContributesAndroidInjector()
    abstract fun bindFullExamDateActivity() : FullExamDateActivity

}