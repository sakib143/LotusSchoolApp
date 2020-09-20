package com.learnathome.di

import com.learnathome.ui.home.HomeActivity
import com.learnathome.ui.home.HomeBuilderModule
import com.learnathome.ui.auth.login.LoginActivity
import com.learnathome.ui.splash.SplashActivity
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