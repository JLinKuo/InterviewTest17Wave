package com.example.mvvmcodebase.model.network

import com.example.interviewtest17wave.model.network.bean.SearchUsersResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/search/users")
    suspend fun searchUsers(
        @Query("q") queryData: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
    ): SearchUsersResponse
}