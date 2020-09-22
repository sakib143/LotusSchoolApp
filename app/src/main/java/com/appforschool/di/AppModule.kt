package com.appforschool.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.appforschool.MyApp
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * An Utility class that having ViewModelModule for binding ViewModels
 * that provides App, context and resources to the scope
 * */
@Module(includes = [(ViewModelModule::class)])
class AppModule {

    @Singleton
    @Provides
    fun provideApp(app: Application): MyApp {
        return app as MyApp
    }

    @Singleton
    @Provides
    fun provideContext(app: Application): Context {
        return app.applicationContext
    }

    @Singleton
    @Provides
    fun provideResource(context: Context): Resources {
        return context.resources
    }

}