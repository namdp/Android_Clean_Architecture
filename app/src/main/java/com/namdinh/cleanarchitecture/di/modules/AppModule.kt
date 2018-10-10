package com.namdinh.cleanarchitecture.di.modules

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.namdinh.cleanarchitecture.di.scopes.PerApplication
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
