package com.namdinh.cleanarchitecture.presentation.user

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.namdinh.cleanarchitecture.R
import com.namdinh.cleanarchitecture.core.helper.AppExecutors
import com.namdinh.cleanarchitecture.core.helper.autoCleared
import com.namdinh.cleanarchitecture.data.remote.helper.rx.Resource
import com.namdinh.cleanarchitecture.databinding.UserFragmentBinding
import com.namdinh.cleanarchitecture.presentation.base.fragment.BaseFragment
import com.namdinh.cleanarchitecture.presentation.base.view.RetryCallback
import com.namdinh.cleanarchitecture.presentation.repo.RepoListAdapter
import javax.inject.Inject

class UserFragment : BaseFragment<UserFragmentBinding, UserViewModel>() {
    @Inject
    lateinit var appExecutors: AppExecutors
    private var adapter by autoCleared<RepoListAdapter>()

    override val layoutId: Int
        get() = R.layout.user_fragment

    override fun initViewModel() = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel::class.java)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(UserViewModel::class.java)
        val params = UserFragmentArgs.fromBundle(arguments)
        viewModel.setLogin(params.login)

        viewModel.user.observe(this, Observer { userResource ->
            binding.userResource = userResource
            when (userResource) {
                is Resource.Loading -> {
                    // loading state was handled by DataBinding
                }
                is Resource.Success -> {
                    binding.user = userResource.data?.get(0)
                }
                is Resource.Failure -> {
                    binding.user = null
                }
            }
        })
        val rvAdapter = RepoListAdapter(appExecutors = appExecutors, showFullName = false) { repo ->
            navController.navigate(UserFragmentDirections.showRepo(repo.owner.login, repo.name))
        }
        binding.rvRepos.adapter = rvAdapter
        this.adapter = rvAdapter
        initRepoList()
        initRetryCallback()
    }

    private fun initRepoList() {
        viewModel.repositories.observe(this, Observer { reposResource ->
            when (reposResource) {
                is Resource.Success -> {
                    adapter.submitList(reposResource.data)
                }
                is Resource.Failure -> {
                    adapter.submitList(emptyList())
                }
            }
        })
    }

    private fun initRetryCallback() {
        binding.retryCallback = object : RetryCallback {
            override fun retry() {
                viewModel.retry()
            }
        }
    }
}
