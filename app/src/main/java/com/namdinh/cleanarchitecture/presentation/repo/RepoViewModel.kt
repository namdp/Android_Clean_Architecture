package com.namdinh.cleanarchitecture.presentation.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.namdinh.cleanarchitecture.core.helper.AbsentLiveData
import com.namdinh.cleanarchitecture.data.remote.helper.rx.Resource
import com.namdinh.cleanarchitecture.domain.repository.RepoRepository
import com.namdinh.cleanarchitecture.domain.vo.Contributor
import com.namdinh.cleanarchitecture.domain.vo.Repo
import com.namdinh.cleanarchitecture.presentation.base.viewmodel.BaseViewModel
import javax.inject.Inject

class RepoViewModel @Inject constructor(repository: RepoRepository) : BaseViewModel() {
    private val repoId: MutableLiveData<RepoId> = MutableLiveData()

    val repo: LiveData<Resource<List<Repo>>> = Transformations
        .switchMap(repoId) { input ->
            input.ifExists { owner, name ->
                LiveDataReactiveStreams.fromPublisher(repository.loadRepo(owner, name))
            }
        }

    val contributors: LiveData<Resource<List<Contributor>>> = Transformations
        .switchMap(repoId) { input ->
            input.ifExists { owner, name ->
                LiveDataReactiveStreams.fromPublisher(repository.loadContributors(owner, name))
            }
        }

    fun retry() {
        val owner = repoId.value?.owner
        val name = repoId.value?.name
        if (owner != null && name != null) {
            repoId.value = RepoId(owner, name)
        }
    }

    fun setId(owner: String, name: String) {
        val update = RepoId(owner, name)
        if (repoId.value == update) {
            return
        }
        repoId.value = update
    }

    data class RepoId(val owner: String, val name: String) {
        fun <T> ifExists(f: (String, String) -> LiveData<T>): LiveData<T> {
            return if (owner.isBlank() || name.isBlank()) {
                AbsentLiveData.create()
            } else {
                f(owner, name)
            }
        }
    }
}
