package com.appforschool.ui.home

import com.appforschool.ui.home.fragment.notification.AlertFragment
import com.appforschool.ui.home.fragment.assignment.AssignmentFragment
import com.appforschool.ui.home.fragment.dashboard.DashboardFragment
import com.appforschool.ui.home.fragment.drive.DriveFragment
import com.appforschool.ui.home.fragment.drive.answer.AnswerFragment
import com.appforschool.ui.home.fragment.drive.mydrive.MyDriveFragment
import com.appforschool.ui.home.fragment.drive.shared.SharedFragment
import com.appforschool.ui.home.fragment.exam.ExamListFragment
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

    /**
     * fun to bind ExamListFragment Fragment, making Injection enable
     */
    @ContributesAndroidInjector
    abstract fun bindExamListFragment(): ExamListFragment


    /**
     * fun to bind ExamListFragment Fragment, making Injection enable
     */
    @ContributesAndroidInjector
    abstract fun bindDriveFragment(): DriveFragment

    /**
     * fun to bind MyDriveFragment Fragment, making Injection enable
     */
    @ContributesAndroidInjector
    abstract fun bindMyDriveFragment(): MyDriveFragment

    /**
     * fun to bind AnswerFragment Fragment, making Injection enable
     */
    @ContributesAndroidInjector
    abstract fun bindAnswerFragment(): AnswerFragment

    /**
     * fun to bind ExamListFragment Fragment, making Injection enable
     */
    @ContributesAndroidInjector
    abstract fun bindSharedFragment(): SharedFragment


}