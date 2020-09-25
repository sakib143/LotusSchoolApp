package com.appforschool.ui.home

import com.appforschool.ui.home.fragment.dashboard.DashboardFragment
import com.appforschool.ui.home.fragment.schedule.ScheduleFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeBuilderModule {
    /**
     * fun to bind Order Schedule Fragment, making Injection enable
     */
    @ContributesAndroidInjector
    abstract fun bindHomeFragment(): ScheduleFragment

    /**
     * fun to bind Order Dashboard Fragment, making Injection enable
     */
    @ContributesAndroidInjector
    abstract fun bindDashboardFragment(): DashboardFragment

}