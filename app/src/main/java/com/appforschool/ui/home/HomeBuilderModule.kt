package com.appforschool.ui.home

import com.appforschool.ui.home.fragment.alert.AlertFragment
import com.appforschool.ui.home.fragment.assignment.AssignmentFragment
import com.appforschool.ui.home.fragment.dashboard.DashboardFragment
import com.appforschool.ui.home.fragment.schedule.ScheduleFragment
import com.appforschool.ui.home.fragment.subject.SubjectFragment
import com.appforschool.ui.home.fragment.subject.subjectdetails.SubjectDetailsFragment
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

    /**
     * fun to bind Order Subject Fragment, making Injection enable
     */
    @ContributesAndroidInjector
    abstract fun bindSubjectFragment(): SubjectFragment

    /**
     * fun to bind Order SubjectDetailsFragment Fragment, making Injection enable
     */
    @ContributesAndroidInjector
    abstract fun bindSubjectDetailsFragment(): SubjectDetailsFragment

    /**
     * fun to bind Order SubjectDetailsFragment Fragment, making Injection enable
     */
    @ContributesAndroidInjector
    abstract fun bindAssignmentFragment(): AssignmentFragment

    /**
     * fun to bind Order AlertFragment Fragment, making Injection enable
     */
    @ContributesAndroidInjector
    abstract fun bindAlertFragment(): AlertFragment

}