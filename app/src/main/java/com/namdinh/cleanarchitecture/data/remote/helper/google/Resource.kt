package com.namdinh.cleanarchitecture.data.remote.helper.google

import com.namdinh.cleanarchitecture.data.remote.helper.google.Status.ERROR
import com.namdinh.cleanarchitecture.data.remote.helper.google.Status.LOADING
import com.namdinh.cleanarchitecture.data.remote.helper.google.Status.SUCCESS

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
data class Resource<out T>(val status: Status, val data: T?, val throwable: Throwable?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(SUCCESS, data, null)
        }

        fun <T> error(throwable: Throwable, data: T?): Resource<T> {
            return Resource(ERROR, data, throwable)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(LOADING, data, null)
        }
    }
}
