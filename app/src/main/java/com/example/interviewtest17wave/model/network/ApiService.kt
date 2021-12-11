package com.example.mvvmcodebase.model.network

import com.example.interviewtest17wave.model.network.bean.SearchUsersResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
//    @Headers("Authorization: token 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")
    @GET("/search/users")
    suspend fun searchUsers(
        @Query("q") queryData: String,
        @Query("per_page") perPage: Int,
        @Query("page") page: Int,
    ): SearchUsersResponse
}