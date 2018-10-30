package com.namdinh.cleanarchitecture.domain.usecase

abstract class BaseUseCase<out Type, in Params> where Type : Any {

    abstract fun execute(params: Params): Type

    class None
}
