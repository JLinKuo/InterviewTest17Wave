package com.example.interviewtest17wave.model.network.bean

import com.google.gson.annotations.SerializedName

data class SearchUsersResponse(
    @SerializedName("total_count")
    var totalCount: Int,
    @SerializedName("incomplete_results")
    var incompleteResults: Boolean,
    @SerializedName("items")
    val listItems: List<GithubUser>
)
