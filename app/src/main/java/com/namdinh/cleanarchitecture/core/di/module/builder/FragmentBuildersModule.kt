package com.namdinh.cleanarchitecture.core.di.module.builder

import com.namdinh.cleanarchitecture.presentation.repo.RepoFragment
import com.namdinh.cleanarchitecture.presentation.search.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeRepoFragment(): RepoFragment

//    @ContributesAndroidInjector
//    abstract fun contributeUserFragment(): UserFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment
}
