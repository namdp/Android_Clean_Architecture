package com.namdinh.cleanarchitecture.domain.usecase

import com.namdinh.cleanarchitecture.data.remote.helper.rx.Resource
import com.namdinh.cleanarchitecture.domain.repository.RepoRepository
import io.reactivex.Single
import javax.inject.Inject

class SearchNextRepositories @Inject constructor(private val repoRepository: RepoRepository)
    : BaseUseCase<Single<Resource<Boolean>>, SearchNextRepositories.Params>() {

    override fun execute(params: Params): Single<Resource<Boolean>> {
        return repoRepository.searchNextPage(params.query)
    }

    data class Params(val query: String)
}
