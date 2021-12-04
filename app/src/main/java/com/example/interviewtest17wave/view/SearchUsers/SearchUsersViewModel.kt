package com.example.interviewtest17wave.view.SearchUsers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.interviewtest17wave.model.network.bean.SearchUsersResponse
import com.example.interviewtest17wave.model.repository.SearchUsersRepository
import com.example.mvvmcodebase.model.network.Resource
import com.example.mvvmcodebase.view.base.BaseViewModel
import kotlinx.coroutines.launch

class SearchUsersViewModel: BaseViewModel() {
    private val repository by lazy { SearchUsersRepository() }

    private val _searchUsersResponse = MutableLiveData<Resource<SearchUsersResponse>>()
    val searchUsersRepository: LiveData<Resource<SearchUsersResponse>>
        get() = _searchUsersResponse

    fun searchUsers(query: String) {
        viewModelScope.launch {
            _searchUsersResponse.value = Resource.Loading
            _searchUsersResponse.value = repository.searchUsers(query)
        }
    }
}