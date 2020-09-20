package com.learnathome.ui.home

import com.learnathome.ui.home.fragment.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class HomeBuilderModule {
    /**
     * fun to bind Order HomeFragment, making Injection enable
     */
    @ContributesAndroidInjector
    abstract fun bindHomeFragment(): HomeFragment

}