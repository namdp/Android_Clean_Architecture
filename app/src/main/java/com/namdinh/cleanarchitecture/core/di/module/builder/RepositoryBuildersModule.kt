package com.namdinh.cleanarchitecture.core.di.module.builder

import com.namdinh.cleanarchitecture.core.helper.AppExecutors
import com.namdinh.cleanarchitecture.data.local.room.GithubDb
import com.namdinh.cleanarchitecture.data.local.room.dao.RepoDao
import com.namdinh.cleanarchitecture.data.local.room.dao.UserDao
import com.namdinh.cleanarchitecture.data.local.room.repositoryimpl.RepoRepositoryImpl
import com.namdinh.cleanarchitecture.data.local.room.repositoryimpl.UserRepositoryImpl
import com.namdinh.cleanarchitecture.data.remote.GithubService
import com.namdinh.cleanarchitecture.domain.repository.RepoRepository
import com.namdinh.cleanarchitecture.domain.repository.UserRepository
import dagger.Module
import dagger.Provides

@Suppress("unused")
@Module
class RepositoryBuildersModule {
    @Provides
    fun providesRepoRepository(
        appExecutors: AppExecutors,
        githubDb: GithubDb,
        githubService: GithubService,
        repoDao: RepoDao
    ): RepoRepository {
        return RepoRepositoryImpl(appExecutors, githubDb, githubService, repoDao)
    }

    @Provides
    fun providesUserRepository(
        appExecutors: AppExecutors,
        githubDb: GithubDb,
        githubService: GithubService,
        userDao: UserDao
    ): UserRepository {
        return UserRepositoryImpl(appExecutors, githubDb, githubService, userDao)
    }
}
