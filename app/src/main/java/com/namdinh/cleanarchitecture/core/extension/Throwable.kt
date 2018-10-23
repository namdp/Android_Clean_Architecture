package com.namdinh.cleanarchitecture.core.extension

import com.namdinh.cleanarchitecture.data.remote.helper.exception.Failure
import io.reactivex.exceptions.Exceptions
import retrofit2.HttpException
import java.io.IOException

fun Throwable.toAppFailure(): Throwable {
    when (this) {
        is HttpException -> throw Exceptions.propagate(Failure.ServerError(this))
        is IOException -> throw Exceptions.propagate(Failure.NetworkConnectionError(this))
        else -> throw Exceptions.propagate(Failure.UnexpectedError(this))
    }
}