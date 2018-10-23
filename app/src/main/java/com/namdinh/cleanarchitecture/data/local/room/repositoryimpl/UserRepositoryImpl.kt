/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.namdinh.cleanarchitecture.data.local.room.repositoryimpl

import com.namdinh.cleanarchitecture.core.helper.AppExecutors
import com.namdinh.cleanarchitecture.core.helper.RateLimiter
import com.namdinh.cleanarchitecture.data.local.room.GithubDb
import com.namdinh.cleanarchitecture.data.local.room.dao.UserDao
import com.namdinh.cleanarchitecture.data.local.room.entity.UserEntity
import com.namdinh.cleanarchitecture.data.remote.GithubService
import com.namdinh.cleanarchitecture.data.remote.helper.google.ApiResponse
import com.namdinh.cleanarchitecture.data.remote.helper.rx.NetworkBoundResourceFlowable
import com.namdinh.cleanarchitecture.data.remote.helper.rx.Resource
import com.namdinh.cleanarchitecture.domain.repository.UserRepository
import com.namdinh.cleanarchitecture.domain.vo.User
import io.reactivex.Flowable
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRepositoryImpl @Inject constructor(appExecutors: AppExecutors,
                                             githubDb: GithubDb,
                                             githubService: GithubService,
                                             private val userDao: UserDao)
    : BaseRepositoryImpl(appExecutors, githubDb, githubService), UserRepository {

    private val userRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)

    override fun loadUser(login: String): Flowable<Resource<List<User>>> {
        return object : NetworkBoundResourceFlowable<List<User>, UserEntity>() {
            override fun onFetchFailed() {
                userRateLimit.reset(login)
            }

            override fun saveCallResult(request: UserEntity) {
                userDao.insert(request)
            }

            override fun shouldFetch(result: List<User>?): Boolean {
                return result == null || result.isEmpty() || userRateLimit.shouldFetch(login)
            }

            override fun loadFromDb(): Flowable<List<User>> {
                return userDao.findByLogin(login).map { userEntities -> userEntities.map { it.toUser() } }
            }

            override fun createCall(): Flowable<ApiResponse<UserEntity>> {
                return githubService.getUser(login)
            }

        }.asFlowable()
    }
}
