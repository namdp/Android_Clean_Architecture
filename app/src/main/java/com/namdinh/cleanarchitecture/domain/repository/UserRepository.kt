package com.namdinh.cleanarchitecture.domain.repository

import com.namdinh.cleanarchitecture.data.remote.helper.rx.Resource
import com.namdinh.cleanarchitecture.domain.vo.User
import io.reactivex.Flowable
import javax.inject.Singleton

@Singleton
interface UserRepository {
    // TODO Room: we wrapped into `List` to get an empty list instead of complete silence if there is no record
    fun loadUser(login: String): Flowable<Resource<List<User>>>
}
