package com.namdinh.cleanarchitecture.data.remote.helper.exception

/**
 * Base Class for handling errors/failures/exceptions.
 * Every feature specific failure should extend [FeatureError] class.
 */
sealed class Failure(cause: Throwable?) : Throwable(null, cause) {
    class NetworkConnectionError(cause: Throwable?) : Failure(cause)
    class ServerError(cause: Throwable?) : Failure(cause)
    class UnexpectedError(cause: Throwable?) : Failure(cause)

    /** * Extend this class for feature specific failures.*/
    abstract class FeatureError(cause: Throwable?) : Failure(cause)
}
