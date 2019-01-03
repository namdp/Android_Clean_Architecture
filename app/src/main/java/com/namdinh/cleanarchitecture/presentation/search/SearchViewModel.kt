package com.namdinh.cleanarchitecture.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.namdinh.cleanarchitecture.core.extension.toAppFailure
import com.namdinh.cleanarchitecture.core.helper.AbsentLiveData
import com.namdinh.cleanarchitecture.data.remote.helper.rx.Resource
import com.namdinh.cleanarchitecture.domain.usecase.SearchNextRepositories
import com.namdinh.cleanarchitecture.domain.usecase.SearchRepositories
import com.namdinh.cleanarchitecture.domain.vo.Repo
import com.namdinh.cleanarchitecture.presentation.base.viewmodel.BaseViewModel
import hu.akarnokd.rxjava2.consumers.SingleConsumers
import java.util.* // ktlint-disable no-wildcard-imports
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchRepositories: SearchRepositories,
    private val searchNextRepositories: SearchNextRepositories
) : BaseViewModel() {

    private val query = MutableLiveData<String>()

    val repositories: LiveData<Resource<List<Repo>>> = Transformations
        .switchMap(query) { it ->
            if (it.isNullOrBlank()) {
                AbsentLiveData.create()
            } else {
                LiveDataReactiveStreams.fromPublisher(
                    searchRepositories.execute(SearchRepositories.Params(query = it)))
            }
        }

    val loadMoreStatus: MutableLiveData<Resource<Boolean>> = MutableLiveData()

    fun loadNextPage() {
        query.value?.let { it ->
            if (it.isNotBlank()) {
                SingleConsumers.subscribeAutoDispose(
                    searchNextRepositories.execute(SearchNextRepositories.Params(it))
                        .doOnSubscribe {
                            loadMoreStatus.value = Resource.Loading()
                        },
                    disposables,
                    { status -> loadMoreStatus.value = status },
                    { throwable -> loadMoreStatus.value = Resource.Failure(throwable.toAppFailure()) })
            }
        }
    }

    fun setQuery(originalInput: String) {
        val input = originalInput.toLowerCase(Locale.getDefault()).trim()
        if (input == query.value) {
            return
        }
        query.value = input
    }

    fun refresh() {
        query.value?.let { query.value = it }
    }
}
