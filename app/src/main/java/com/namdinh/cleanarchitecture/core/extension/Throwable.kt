package com.namdinh.cleanarchitecture.core.extension

import com.namdinh.cleanarchitecture.data.remote.helper.exception.Failure
import retrofit2.HttpException
import java.io.IOException

fun Throwable.toAppFailure(): Throwable {
    return when (this) {
        is Failure -> this
        is HttpException -> Failure.ServerError(this)
        is IOException -> Failure.NetworkConnectionError(this)
        else -> Failure.UnexpectedError(this)
    }
}
