package com.namdinh.cleanarchitecture.core.di.module.builder

import com.namdinh.cleanarchitecture.presentation.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class ActivityBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}
