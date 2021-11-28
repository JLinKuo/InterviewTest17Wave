package com.example.interviewtest17wave.view.SearchUsers

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.interviewtest17wave.databinding.FragmentSearchUsersBinding
import com.example.interviewtest17wave.view.base.handleApiError
import com.example.mvvmcodebase.model.network.Resource
import com.example.mvvmcodebase.view.base.BaseFragment

/**
 * A simple [Fragment] subclass.
 */
class SearchUsersFragment : BaseFragment<SearchUsersViewModel, FragmentSearchUsersBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListener()
        setObserver()
    }

    private fun setListener() {
        binding.searchUsers.setOnClickListener {
            viewModel.searchUsers()
        }
    }

    private fun setObserver() {
        viewModel.searchUsersRepository.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    it.value.listItems.forEach { item ->
                        Log.d("JLin", "avatar: ${item.avatarUrl}")
                    }
                    activity.dismissProgressBar()
                }
                is Resource.Failure -> handleApiError(it)
                is Resource.Loading -> activity.showProgressBar(true)
            }
        }
    }

    override fun getViewModel() = SearchUsersViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchUsersBinding  = FragmentSearchUsersBinding.inflate(inflater, container, false)

}