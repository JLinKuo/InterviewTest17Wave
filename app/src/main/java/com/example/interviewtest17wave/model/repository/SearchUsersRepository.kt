package com.example.interviewtest17wave.model.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchUsersRepository: BaseRepository() {
    suspend fun searchUsers() = safeApiCall {
        withContext(Dispatchers.IO) {
            apiService.searchUsers("qoo", 30, 1)
        }
    }
}