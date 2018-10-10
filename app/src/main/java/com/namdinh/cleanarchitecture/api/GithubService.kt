package com.namdinh.cleanarchitecture.api

import androidx.lifecycle.LiveData
import com.namdinh.cleanarchitecture.api.helpers.ApiResponse
import com.namdinh.cleanarchitecture.api.responses.RepoSearchResponse
import com.namdinh.cleanarchitecture.entities.ContributorEntity
import com.namdinh.cleanarchitecture.entities.RepoEntity
import com.namdinh.cleanarchitecture.entities.UserEntity
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubService {
    @GET("users/{login}")
    fun getUser(@Path("login") login: String): LiveData<ApiResponse<UserEntity>>

    @GET("users/{login}/repos")
    fun getRepos(@Path("login") login: String): LiveData<ApiResponse<List<RepoEntity>>>

    @GET("repos/{owner}/{name}")
    fun getRepo(@Path("owner") owner: String, @Path("name") name: String): LiveData<ApiResponse<RepoEntity>>

    @GET("repos/{owner}/{name}/contributors")
    fun getContributors(@Path("owner") owner: String, @Path("name") name: String): LiveData<ApiResponse<List<ContributorEntity>>>

    @GET("search/repositories")
    fun searchRepos(@Query("q") query: String): LiveData<ApiResponse<RepoSearchResponse>>

    @GET("search/repositories")
    fun searchRepos(@Query("q") query: String, @Query("page") page: Int): Call<RepoSearchResponse>

}