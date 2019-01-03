package com.namdinh.cleanarchitecture.domain.repository

import com.namdinh.cleanarchitecture.data.remote.helper.rx.Resource
import com.namdinh.cleanarchitecture.domain.vo.Contributor
import com.namdinh.cleanarchitecture.domain.vo.Repo
import io.reactivex.Flowable
import io.reactivex.Single
import javax.inject.Singleton

@Singleton
interface RepoRepository {

    fun loadRepos(owner: String): Flowable<Resource<List<Repo>>>

    // TODO Room: we wrapped into `List` to get an empty list instead of complete silence if there is no record
    fun loadRepo(owner: String, name: String): Flowable<Resource<List<Repo>>>

    fun loadContributors(owner: String, name: String): Flowable<Resource<List<Contributor>>>

    fun search(query: String): Flowable<Resource<List<Repo>>>

    // TODO Room: we wrapped into `List` to get an empty list instead of complete silence if there is no record
    fun searchNextPage(query: String): Single<Resource<Boolean>>
}
