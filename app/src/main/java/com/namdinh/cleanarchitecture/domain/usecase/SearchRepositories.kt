package com.namdinh.cleanarchitecture.domain.usecase

import com.namdinh.cleanarchitecture.data.remote.helper.rx.Resource
import com.namdinh.cleanarchitecture.domain.repository.RepoRepository
import com.namdinh.cleanarchitecture.domain.vo.Repo
import io.reactivex.Flowable
import javax.inject.Inject

class SearchRepositories @Inject constructor(private val repoRepository: RepoRepository)
    : BaseUseCase<Flowable<Resource<List<Repo>>>, SearchRepositories.Params>() {

    override fun execute(params: Params): Flowable<Resource<List<Repo>>> {
        return repoRepository.search(params.query)
    }

    data class Params(val query: String)
}
