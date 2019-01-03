package com.namdinh.cleanarchitecture.presentation.repo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.namdinh.cleanarchitecture.R
import com.namdinh.cleanarchitecture.core.helper.AppExecutors
import com.namdinh.cleanarchitecture.databinding.ContributorItemBinding
import com.namdinh.cleanarchitecture.domain.vo.Contributor
import com.namdinh.cleanarchitecture.presentation.base.view.DataBoundListAdapter

class ContributorAdapter(
    appExecutors: AppExecutors,
    private val callback: ((Contributor) -> Unit)?
) : DataBoundListAdapter<Contributor, ContributorItemBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<Contributor>() {
        override fun areItemsTheSame(oldItem: Contributor, newItem: Contributor): Boolean {
            return oldItem.login == newItem.login
        }

        override fun areContentsTheSame(oldItem: Contributor, newItem: Contributor): Boolean {
            return oldItem.avatarUrl == newItem.avatarUrl &&
                oldItem.contributions == newItem.contributions
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ContributorItemBinding {
        val binding = DataBindingUtil.inflate<ContributorItemBinding>(
            LayoutInflater.from(parent.context), R.layout.contributor_item, parent, false)
        binding.root.setOnClickListener { _ -> binding.contributor?.let { callback?.invoke(it) } }
        return binding
    }

    override fun bind(binding: ContributorItemBinding, item: Contributor) {
        binding.contributor = item
    }
}
