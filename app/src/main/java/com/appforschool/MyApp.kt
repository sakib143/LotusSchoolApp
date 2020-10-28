package com.appforschool

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.appforschool.di.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class MyApp  : Application(), HasAndroidInjector {

    /**
     * @see [dagger.android.DispatchingAndroidInjector]
     * */
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    /**
     * @see [dagger.android.DispatchingAndroidInjector]
     * */
    override fun androidInjector() = dispatchingAndroidInjector

    /**
     * this method gets called when the Application gets created
     * */
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        AppInjector.init(this)
    }
}