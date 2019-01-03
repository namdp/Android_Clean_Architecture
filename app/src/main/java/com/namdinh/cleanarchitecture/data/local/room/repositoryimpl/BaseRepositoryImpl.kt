package com.namdinh.cleanarchitecture.data.local.room.repositoryimpl

import com.namdinh.cleanarchitecture.core.helper.AppExecutors
import com.namdinh.cleanarchitecture.data.local.room.GithubDb
import com.namdinh.cleanarchitecture.data.remote.GithubService

abstract class BaseRepositoryImpl(
    protected var appExecutors: AppExecutors,
    protected var githubDb: GithubDb,
    protected var githubService: GithubService
)
