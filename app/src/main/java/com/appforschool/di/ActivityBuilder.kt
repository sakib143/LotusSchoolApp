package com.appforschool.di

import com.appforschool.ui.home.HomeActivity
import com.appforschool.ui.home.HomeBuilderModule
import com.appforschool.ui.auth.login.LoginActivity
import com.appforschool.ui.commonwebview.CommonWebviewActivity
import com.appforschool.ui.splash.SplashActivity
import com.appforschool.ui.videocalling.VideoCallingActivity
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
     * fun to bind VideoCalling screen , making Injection enable
     **/
    @ContributesAndroidInjector()
    abstract fun bindVideoCallingActivity(): VideoCallingActivity



//
//    /**
//     * fun to bind Help Activity, making Injection enable
//     **/
//    @ContributesAndroidInjector(modules = [(HelpScreenModule::class)])
//    abstract fun bindHomeNewActivity() : HelpScreenActivity
//
//    /**
//     * fun to bind Home  Activity, making Injection enable
//     **/
//    @ContributesAndroidInjector()
//    abstract fun bindHomeActivity() : HomeActivity
//
//    /**
//     * fun to bind Video calling screen, making Injection enable
//     **/
//    @ContributesAndroidInjector()
//    abstract fun bindVideoCallingActivity() : VideoCallingActivity

}