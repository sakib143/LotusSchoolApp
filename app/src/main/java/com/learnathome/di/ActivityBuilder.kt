package com.learnathome.di

import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Helps to generate an {@link AndroidInjector} for all activities
 * */
@Suppress("unused")
@Module
abstract class ActivityBuilder {

//    /**
//     * fun to bind Splash Activity, making Injection enable
//     **/
//    @ContributesAndroidInjector()
//    abstract fun bindSplashActivity() : SplashActivity
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