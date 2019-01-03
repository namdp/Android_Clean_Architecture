package com.namdinh.cleanarchitecture.data.remote.helper.rx

import com.namdinh.cleanarchitecture.core.extension.toAppFailure
import com.namdinh.cleanarchitecture.data.remote.helper.google.ApiEmptyResponse
import com.namdinh.cleanarchitecture.data.remote.helper.google.ApiErrorResponse
import com.namdinh.cleanarchitecture.data.remote.helper.google.ApiResponse
import com.namdinh.cleanarchitecture.data.remote.helper.google.ApiSuccessResponse
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import timber.log.Timber

/* @Note: Room version 2.0.0
 * - `Single` and `Maybe` are suitable for one-time data retrieval.
 *   + The difference between that Maybe is admitting that there may be a record.
 *   + And Single is more logical if the record should be in the database if not you will get an error `EmptyResultSetException`.
 *
 * - `Flowable` is more suitable if you plan to listen to the data automatically when it changes.
 *   + Single query -> if there is no record will not emit, neither onNext, nor onError, only call onCompleted silence
 *   + List query -> if there is no record, then at least we’ll get an empty list instead of complete silence
 *
 * - Reference: https://codinginfinite.com/android-room-persistent-rxjava
 */
abstract class NetworkBoundResourceFlowable<ResultType, RequestType> {

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
                .map {
                    val response = ApiResponse.create(it)
                    when (response) {
                        is ApiSuccessResponse -> saveCallResult(processResponse(response))
                        is ApiErrorResponse -> throw response.throwable
                        is ApiEmptyResponse -> Timber.d("ApiEmptyResponse")
                    }
                }
                .flatMap { loadFromDb() }
        }

        // When there is no data in the Room database and the query returns no rows,
        // the Flowable will not emit, neither onNext, nor onError
        // -> ResultType must wrapped by List to get at least we’ll get an empty list instead of complete silence.
        result = diskObservable
            .flatMap<Resource<ResultType>> { resultData ->
                if (shouldFetch(resultData)) {
                    networkObservable.map<Resource<ResultType>> { Resource.Success(it) }
                } else {
                    Flowable.just(Resource.Success(resultData))
                }
            }
            .onErrorReturn {
                onFetchFailed()
                Resource.Failure(it.toAppFailure())
            }
            .startWith(Resource.Loading())
            // Read results in Android Main Thread (UI)
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun asFlowable(): Flowable<Resource<ResultType>> {
        return result
    }

    protected open fun onFetchFailed() {}

    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    protected abstract fun saveCallResult(request: RequestType)

    protected abstract fun shouldFetch(result: ResultType?): Boolean

    protected abstract fun loadFromDb(): Flowable<ResultType>

    protected abstract fun createCall(): Flowable<Response<RequestType>>
}
