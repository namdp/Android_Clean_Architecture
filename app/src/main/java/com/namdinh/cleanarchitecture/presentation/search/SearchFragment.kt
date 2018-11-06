package com.namdinh.cleanarchitecture.presentation.search

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.namdinh.cleanarchitecture.R
import com.namdinh.cleanarchitecture.core.extension.dismissKeyboard
import com.namdinh.cleanarchitecture.core.helper.AppExecutors
import com.namdinh.cleanarchitecture.core.helper.autoCleared
import com.namdinh.cleanarchitecture.data.remote.helper.rx.Resource
import com.namdinh.cleanarchitecture.databinding.SearchFragmentBinding
import com.namdinh.cleanarchitecture.presentation.base.fragment.BaseFragment
import com.namdinh.cleanarchitecture.presentation.base.view.RetryCallback
import com.namdinh.cleanarchitecture.presentation.repo.RepoListAdapter
import javax.inject.Inject

class SearchFragment : BaseFragment<SearchFragmentBinding, SearchViewModel>() {
    @Inject
    lateinit var appExecutors: AppExecutors
    var adapter by autoCleared<RepoListAdapter>()

    override val layoutId: Int get() = R.layout.search_fragment

    override fun initViewModel() = ViewModelProviders.of(this, viewModelFactory)[SearchViewModel::class.java]

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        val rvAdapter = RepoListAdapter(appExecutors = appExecutors, showFullName = true)
        { repo ->
            //@todo
            // navController.navigate(SearchFragmentDirections.showRepo(repo.owner.login, repo.name))
        }
        binding.rvRepo.adapter = rvAdapter
        adapter = rvAdapter

        initSearchInputListener()

        binding.callback = object : RetryCallback {
            override fun retry() {
                viewModel.refresh()
            }
        }
    }

    private fun initSearchInputListener() {
        binding.etSearch.setOnEditorActionListener { view: View, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch(view)
                true
            } else {
                false
            }
        }
        binding.etSearch.setOnKeyListener { view: View, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                doSearch(view)
                true
            } else {
                false
            }
        }
    }

    private fun doSearch(v: View) {
        context?.dismissKeyboard(v.windowToken)
        val query = binding.etSearch.text.toString()
        binding.query = query
        viewModel.setQuery(query)
    }

    private fun initRecyclerView() {
        binding.rvRepo.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val lastPosition = layoutManager.findLastVisibleItemPosition()
                if (lastPosition == adapter.itemCount - 1) {
                    viewModel.loadNextPage()
                }
            }
        })
        viewModel.repositories.observe(this, Observer { result ->
            binding.searchResource = result //binding loading(error) state
            when (result) {
                is Resource.Loading -> {
                    // loading state was handled by DataBinding
                }
                is Resource.Success -> {
                    binding.resultCount = result.data?.size ?: 0
                    adapter.submitList(result.data)
                }
                is Resource.Failure -> {
                    binding.resultCount = 0
                    adapter.submitList(null)
                }
            }
        })

        viewModel.loadMoreStatus.observe(this, Observer { loadingMore ->
            if (loadingMore == null) {
                binding.loadingMore = false
            } else {
                when (loadingMore) {
                    is Resource.Loading -> {
                        binding.loadingMore = true
                    }
                    is Resource.Success -> {
                        binding.loadingMore = false
                    }
                    is Resource.Failure -> {
                        binding.loadingMore = false
                        val error = loadingMore.getFailureMessage()
                        if (error != null && error.isNotBlank()) {
                            Snackbar.make(binding.pbLoadMore, error, Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        })
    }
}
