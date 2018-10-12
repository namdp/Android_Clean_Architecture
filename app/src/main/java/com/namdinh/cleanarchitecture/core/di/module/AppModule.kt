package com.namdinh.cleanarchitecture.core.di.module

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.namdinh.cleanarchitecture.core.di.scope.PerApplication
import dagger.Module
import dagger.Provides

@Module(includes = [(ViewModelModule::class)])
class AppModule {

    @Provides
    @PerApplication
    internal fun provideSharedPreferences(application: Application): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(application)
    }
}
