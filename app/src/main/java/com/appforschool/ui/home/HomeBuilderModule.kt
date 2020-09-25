package com.appforschool.ui.home

import com.appforschool.ui.home.fragment.schedule.ScheduleFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeBuilderModule {
    /**
     * fun to bind Order HomeFragment, making Injection enable
     */
    @ContributesAndroidInjector
    abstract fun bindHomeFragment(): ScheduleFragment

}