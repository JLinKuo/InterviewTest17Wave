package com.example.interviewtest17wave.view.SearchUsers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.interviewtest17wave.databinding.FragmentSearchUsersBinding
import com.example.interviewtest17wave.model.network.bean.GithubUser
import com.example.interviewtest17wave.view.base.handleApiError
import com.example.mvvmcodebase.model.network.Resource
import com.example.mvvmcodebase.view.base.BaseFragment

/**
 * A simple [Fragment] subclass.
 */
class SearchUsersFragment : BaseFragment<SearchUsersViewModel, FragmentSearchUsersBinding>() {

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
        binding.searchUsers.setOnClickListener {
            viewModel.isLoading = true
            viewModel.searchUsers(binding.query.text.toString())
            hideSoftwareKeyboard()
        }
    }

    private fun hideSoftwareKeyboard() {
        activity.currentFocus?.let { view ->
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun setObserver() {
        viewModel.searchUsersRepository.observe(viewLifecycleOwner) {
            when(it) {
                is Resource.Success -> {
                    viewModel.isLoading = false
                    viewModel.nextPage += 1
                    listAdapter.updateList(it.value.listItems)
                    activity.dismissProgressBar()
                }
                is Resource.Failure -> handleApiError(it)
                is Resource.Loading -> activity.showProgressBar(true)
            }
        }
    }

    private fun go2UserGithubByBrowser(githubUser: GithubUser) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(githubUser.htmlUrl))
        startActivity(intent)
    }

    override fun getViewModel() = SearchUsersViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchUsersBinding  = FragmentSearchUsersBinding.inflate(inflater, container, false)
}