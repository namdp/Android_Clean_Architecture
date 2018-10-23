package com.namdinh.cleanarchitecture.data.remote

import com.namdinh.cleanarchitecture.data.local.room.entity.ContributorEntity
import com.namdinh.cleanarchitecture.data.local.room.entity.RepoEntity
import com.namdinh.cleanarchitecture.data.local.room.entity.UserEntity
import com.namdinh.cleanarchitecture.data.remote.helper.google.ApiResponse
import com.namdinh.cleanarchitecture.data.remote.response.RepoSearchResponse
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    @GET("users/{login}")
    fun getUser(@Path("login") login: String): Flowable<ApiResponse<UserEntity>>

    @GET("users/{login}/repos")
    fun getRepos(@Path("login") login: String): Flowable<ApiResponse<List<RepoEntity>>>

    @GET("repos/{owner}/{name}")
    fun getRepo(@Path("owner") owner: String, @Path("name") name: String): Flowable<ApiResponse<RepoEntity>>

    @GET("repos/{owner}/{name}/contributors")
    fun getContributors(@Path("owner") owner: String, @Path("name") name: String): Flowable<ApiResponse<List<ContributorEntity>>>

    @GET("search/repositories")
    fun searchRepos(@Query("q") query: String): Flowable<ApiResponse<RepoSearchResponse>>

    @GET("search/repositories")
    fun searchRepos(@Query("q") query: String, @Query("page") page: Int): Flowable<ApiResponse<RepoSearchResponse>>

}