package com.namdinh.cleanarchitecture.data.remote

import com.namdinh.cleanarchitecture.data.local.room.entity.ContributorEntity
import com.namdinh.cleanarchitecture.data.local.room.entity.RepoEntity
import com.namdinh.cleanarchitecture.data.local.room.entity.UserEntity
import com.namdinh.cleanarchitecture.data.remote.response.RepoSearchResponse
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    @GET("users/{login}")
    fun getUser(@Path("login") login: String): Flowable<Response<UserEntity>>

    @GET("users/{login}/repos")
    fun getRepos(@Path("login") login: String): Flowable<Response<List<RepoEntity>>>

    @GET("repos/{owner}/{name}")
    fun getRepo(@Path("owner") owner: String, @Path("name") name: String): Flowable<Response<RepoEntity>>

    @GET("repos/{owner}/{name}/contributors")
    fun getContributors(@Path("owner") owner: String, @Path("name") name: String): Flowable<Response<List<ContributorEntity>>>

    @GET("search/repositories")
    fun searchRepos(@Query("q") query: String): Flowable<Response<RepoSearchResponse>>

    @GET("search/repositories")
    fun searchRepos(@Query("q") query: String, @Query("page") page: Int): Single<Response<RepoSearchResponse>>
}
