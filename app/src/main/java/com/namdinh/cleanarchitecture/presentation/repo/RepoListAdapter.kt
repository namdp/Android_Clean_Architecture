package com.namdinh.cleanarchitecture.presentation.repo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.namdinh.cleanarchitecture.R
import com.namdinh.cleanarchitecture.core.helper.AppExecutors
import com.namdinh.cleanarchitecture.databinding.RepoItemBinding
import com.namdinh.cleanarchitecture.domain.vo.Repo
import com.namdinh.cleanarchitecture.presentation.base.view.DataBoundListAdapter

/**
 * A RecyclerView adapter for [Repo] class.
 */
class RepoListAdapter(
    appExecutors: AppExecutors,
    private val showFullName: Boolean,
    private val repoClickCallback: ((Repo) -> Unit)?
) : DataBoundListAdapter<Repo, RepoItemBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Repo>() {

        override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
            return oldItem.owner == newItem.owner &&
                oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
            return oldItem.description == newItem.description &&
                oldItem.stars == newItem.stars
        }
    }
) {

    override fun createBinding(parent: ViewGroup): RepoItemBinding {
        val binding = DataBindingUtil.inflate<RepoItemBinding>(
            LayoutInflater.from(parent.context), R.layout.repo_item, parent, false)
        binding.showFullName = showFullName
        binding.root.setOnClickListener { _ -> binding.repo?.let { repoClickCallback?.invoke(it) } }
        return binding
    }

    override fun bind(binding: RepoItemBinding, item: Repo) {
        binding.repo = item
    }
}
