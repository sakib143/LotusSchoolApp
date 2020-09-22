package com.appforschool.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.appforschool.MyApp
import dagger.android.AndroidInjection
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection

/**
 * Helper class to automatically inject fragments if they implement [Injectable].
 */
object AppInjector {
    fun init(application: MyApp) {
        DaggerAppComponent.builder().application(application).build().inject(application)
        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                handleActivity(activity)
            }

            override fun onActivityStarted(activity: Activity) {
                // Do nothing
            }

            override fun onActivityResumed(activity: Activity) {
                // Do nothing
            }

            override fun onActivityPaused(activity: Activity) {
                // Do nothing
            }

            override fun onActivityStopped(activity: Activity) {
                // Do nothing
            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {
                // Do nothing
            }
        })
    }

    /**
     * This will handle the injection of [Activity]
     */
    private fun handleActivity(activity: Activity) {
        if (activity is Injectable || activity is HasAndroidInjector) {
            AndroidInjection.inject(activity)
        }
        (activity as? FragmentActivity)?.supportFragmentManager?.registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentCreated(
                    fm: FragmentManager,
                    f: Fragment,
                    savedInstanceState: Bundle?
                ) {
                    super.onFragmentCreated(fm, f, savedInstanceState)
                    if (f is Injectable) {
                        AndroidSupportInjection.inject(f)
                    }
                }
            }, true
        )
    }
}
