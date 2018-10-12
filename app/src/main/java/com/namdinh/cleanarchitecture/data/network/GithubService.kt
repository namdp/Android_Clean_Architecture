package com.namdinh.cleanarchitecture.data.network

import androidx.lifecycle.LiveData
import com.namdinh.cleanarchitecture.data.db.room.entity.ContributorEntity
import com.namdinh.cleanarchitecture.data.db.room.entity.RepoEntity
import com.namdinh.cleanarchitecture.data.db.room.entity.UserEntity
import com.namdinh.cleanarchitecture.data.network.helper.google.ApiResponse
import com.namdinh.cleanarchitecture.data.network.response.RepoSearchResponse
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