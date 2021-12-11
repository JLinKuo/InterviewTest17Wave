package com.example.interviewtest17wave.view.SearchUsers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.interviewtest17wave.R
import com.example.interviewtest17wave.databinding.FragmentSearchUsersBinding
import com.example.interviewtest17wave.model.network.bean.GithubUser
import com.example.interviewtest17wave.view.base.handleApiError
import com.example.mvvmcodebase.model.network.Resource
import com.example.mvvmcodebase.view.base.BaseFragment
import kotlinx.coroutines.*

/**
 * A simple [Fragment] subclass.
 */
class SearchUsersFragment : BaseFragment<SearchUsersViewModel, FragmentSearchUsersBinding>() {

    private var newJob: Job? = null

    private val listAdapter by lazy {
        SearchUserItemAdapter { githubUser ->
            go2UserGithubByBrowser(githubUser)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setView()
        setListener()
        setObserver()
    }

    private fun setView() {
        val layoutManager = LinearLayoutManager(activity)
        binding.listView.layoutManager = layoutManager
        binding.listView.adapter = listAdapter
        binding.listView.addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        binding.listView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            private var firstVisibleItemPosition = 0
            private var lastVisibleItemPosition = 0

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if(!viewModel.isLoading && newState == RecyclerView.SCROLL_STATE_IDLE &&
                    lastVisibleItemPosition >= listAdapter.getUserList().size - 10) {

                    viewModel.isLoading = true
                    viewModel.searchUsers(binding.query.text.toString())
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            }
        })
    }

    private fun setListener() {
        binding.query.addTextChangedListener(object: TextWatcher{
            override fun onTextChanged(string: CharSequence?, start: Int, before: Int, count: Int) {
                binding.message.text = getString(R.string.search_empty)
                cancelPreviousQuery()

                listAdapter.clearList()

                if(string != null && string.isBlank()) {
                    binding.message.visibility = VISIBLE
                    return
                }

                binding.message.visibility = GONE
                viewModel.isLoading = true
                viewModel.nextPage = 1
                newJob = lifecycleScope.launch {
                    delay(800)
                    viewModel.searchUsers(binding.query.text.toString())
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun cancelPreviousQuery() {
        newJob?.let {
            if(it.isActive || it.isCompleted) {
                newJob?.cancel()
            }
        }
    }

    private fun setObserver() {
        viewModel.searchUsersRepository.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    viewModel.isLoading = false
                    viewModel.nextPage += 1

                    if(it.value.listItems.isNotEmpty()) {
                        listAdapter.updateList(it.value.listItems)
                    }

                    if(listAdapter.getUserList().size > 0) {
                        binding.message.visibility = GONE
                    } else {
                        binding.message.visibility = VISIBLE
                    }

                    binding.progressBar.visibility = GONE
                }
                is Resource.Failure -> {
                    handleApiError(it) { errorCode ->
                        updateViewWitHttpError(errorCode)
                    }

                    binding.progressBar.visibility = GONE
                }
                is Resource.Loading -> binding.progressBar.visibility = VISIBLE
            }
        }
    }

    private fun go2UserGithubByBrowser(githubUser: GithubUser) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(githubUser.htmlUrl))
        startActivity(intent)
    }

    private fun updateViewWitHttpError(errorCode: Int) {
        when(errorCode) {
            403 -> {
                binding.message.text = getString(R.string.search_http_error)
                binding.message.visibility = VISIBLE
            }
        }
    }

    override fun getViewModel() = SearchUsersViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchUsersBinding  = FragmentSearchUsersBinding.inflate(inflater, container, false)
}