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

package com.namdinh.cleanarchitecture.api.helpers.rx

import com.namdinh.cleanarchitecture.api.helpers.exceptions.Failure
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.exceptions.Exceptions
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class NetworkBoundResourceRx<ResultType, RequestType>() {

    private val result: Flowable<Resource<ResultType>>

    init {
        // Lazy disk observable.
        val diskObservable = Flowable.defer {
            loadFromDb()
                    // Read from disk on Computation Scheduler
                    .subscribeOn(Schedulers.computation())
        }

        // Lazy network observable.
        val networkObservable = Flowable.defer {
            createCall()
                    // Request API on IO Scheduler
                    .subscribeOn(Schedulers.io())
                    // Read/Write to disk on Computation Scheduler
                    .observeOn(Schedulers.computation())
                    .doOnNext { request: Response<RequestType> ->
                        if (request.isSuccessful) {
                            saveCallResult(processResponse(request))
                        }
                    }
                    .onErrorReturn { throwable: Throwable ->
                        when (throwable) {
                            is HttpException -> {
                                throw Exceptions.propagate(Failure.ServerError(throwable))
                            }
                            is IOException -> {
                                throw Exceptions.propagate(Failure.NetworkConnection(throwable))
                            }
                            else -> {
                                throw Exceptions.propagate(Failure.UnexpectedError(throwable))
                            }
                        }
                    }
                    .flatMap { loadFromDb() }
        }

        result = diskObservable
                .flatMap<Resource<ResultType>> { resultType ->
                    if (shouldFetch(resultType)) {
                        networkObservable
                                .map<Resource<ResultType>> { Resource.Success(resultType) }
                                .onErrorReturn { Resource.Failure(it) }
                                // Read results in Android Main Thread (UI)
                                .observeOn(AndroidSchedulers.mainThread())
                    } else {
                        Flowable.just(Resource.Success(resultType))
                    }
                }
                .onErrorReturn { Resource.Failure(it) }
                // Read results in Android Main Thread (UI)
                .observeOn(AndroidSchedulers.mainThread())
                .startWith(Resource.Loading())
    }

    fun asFlowable(): Flowable<Resource<ResultType>> {
        return result
    }

    private fun processResponse(response: Response<RequestType>): RequestType? {
        return response.body()
    }

    protected abstract fun saveCallResult(request: RequestType?)

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract fun loadFromDb(): Flowable<ResultType>

    protected abstract fun createCall(): Flowable<Response<RequestType>>
}