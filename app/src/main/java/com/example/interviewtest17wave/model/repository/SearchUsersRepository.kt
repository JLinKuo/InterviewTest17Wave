package com.example.interviewtest17wave.model.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchUsersRepository: BaseRepository() {
    suspend fun searchUsers(query: String) = safeApiCall {
        withContext(Dispatchers.IO) {
            apiService.searchUsers(query, 30, 1)
        }
    }
}