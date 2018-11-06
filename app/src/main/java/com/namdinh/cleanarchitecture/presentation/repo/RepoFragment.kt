package com.namdinh.cleanarchitecture.presentation.repo

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.namdinh.cleanarchitecture.R
import com.namdinh.cleanarchitecture.core.helper.AppExecutors
import com.namdinh.cleanarchitecture.core.helper.autoCleared
import com.namdinh.cleanarchitecture.data.remote.helper.rx.Resource
import com.namdinh.cleanarchitecture.databinding.RepoFragmentBinding
import com.namdinh.cleanarchitecture.presentation.base.fragment.BaseFragment
import com.namdinh.cleanarchitecture.presentation.base.view.RetryCallback
import javax.inject.Inject

/**
 * The UI Controller for displaying a Github Repo's information with its contributors.
 */
class RepoFragment : BaseFragment<RepoFragmentBinding, RepoViewModel>() {
    @Inject
    lateinit var appExecutors: AppExecutors
    private var adapter by autoCleared<ContributorAdapter>()

    override val layoutId: Int
        get() = R.layout.repo_fragment

    override fun initViewModel() = ViewModelProviders.of(this, viewModelFactory)[RepoViewModel::class.java]

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val params = RepoFragmentArgs.fromBundle(arguments)
        viewModel.setId(params.owner, params.name)

        val repo = viewModel.repo
        repo.observe(this, Observer { resource ->
            binding.repoResource = resource
            when (resource) {
                is Resource.Loading -> {
                    // loading state was handled by DataBinding
                }
                is Resource.Success -> {
                    binding.repo = resource.data?.get(0)
                }
                is Resource.Failure -> {
                    binding.repo = null
                }
            }
        })

        val adapter = ContributorAdapter(appExecutors) { contributor ->
            navController.navigate(RepoFragmentDirections.showUser(contributor.login))
        }
        this.adapter = adapter
        binding.rvContributors.adapter = adapter
        initContributorList(viewModel)
        initRetryCallback()
    }

    private fun initContributorList(viewModel: RepoViewModel) {
        viewModel.contributors.observe(this, Observer { listResource ->
            when (listResource) {
                is Resource.Loading -> {
                    // loading state was handled by DataBinding
                }
                is Resource.Success -> {
                    adapter.submitList(listResource.data)
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
