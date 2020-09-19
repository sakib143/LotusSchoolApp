package com.learnathome.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.learnathome.di.ViewModelFactory
import com.learnathome.di.ViewModelKey
import com.learnathome.ui.login.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * This class used to bind ViewModels
 * */
@Suppress("unused")
@Module
abstract class ViewModelModule {

    /**
     * To Bind ViewModelFactory
     * */
    @Binds
    abstract fun bindsViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    /**
     * To Bind HomeViewModel
     * */
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindMainViewModel(viewModel: LoginViewModel): ViewModel

}