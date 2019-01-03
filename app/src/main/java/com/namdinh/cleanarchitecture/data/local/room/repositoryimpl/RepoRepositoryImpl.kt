package com.namdinh.cleanarchitecture.data.local.room.repositoryimpl

import com.namdinh.cleanarchitecture.core.extension.toAppFailure
import com.namdinh.cleanarchitecture.core.helper.AppExecutors
import com.namdinh.cleanarchitecture.core.helper.RateLimiter
import com.namdinh.cleanarchitecture.data.local.room.GithubDb
import com.namdinh.cleanarchitecture.data.local.room.dao.RepoDao
import com.namdinh.cleanarchitecture.data.local.room.entity.ContributorEntity
import com.namdinh.cleanarchitecture.data.local.room.entity.RepoEntity
import com.namdinh.cleanarchitecture.data.local.room.entity.RepoSearchResultEntity
import com.namdinh.cleanarchitecture.data.remote.GithubService
import com.namdinh.cleanarchitecture.data.remote.helper.google.ApiEmptyResponse
import com.namdinh.cleanarchitecture.data.remote.helper.google.ApiErrorResponse
import com.namdinh.cleanarchitecture.data.remote.helper.google.ApiResponse
import com.namdinh.cleanarchitecture.data.remote.helper.google.ApiSuccessResponse
import com.namdinh.cleanarchitecture.data.remote.helper.rx.NetworkBoundResourceFlowable
import com.namdinh.cleanarchitecture.data.remote.helper.rx.Resource
import com.namdinh.cleanarchitecture.data.remote.response.RepoSearchResponse
import com.namdinh.cleanarchitecture.domain.repository.RepoRepository
import com.namdinh.cleanarchitecture.domain.vo.Contributor
import com.namdinh.cleanarchitecture.domain.vo.Repo
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.* // ktlint-disable no-wildcard-imports
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * unfortunate naming :/ .
 * Repo - value object name
 * Repository - type of this class.
 */
@Singleton
class RepoRepositoryImpl @Inject constructor(
    appExecutors: AppExecutors,
    githubDb: GithubDb,
    githubService: GithubService,
    private val repoDao: RepoDao
) : BaseRepositoryImpl(appExecutors, githubDb, githubService), RepoRepository {

    companion object {
        private const val timeout = 30
    }

    private val repoListRateLimit = RateLimiter<String>(timeout, TimeUnit.MINUTES)
    private val repoRateLimit = RateLimiter<String>(timeout, TimeUnit.MINUTES)
    private val contributorListRateLimit = RateLimiter<String>(timeout, TimeUnit.MINUTES)
    private val searchListRateLimit = RateLimiter<String>(timeout, TimeUnit.MINUTES)

    override fun loadRepos(owner: String): Flowable<Resource<List<Repo>>> {
        return object : NetworkBoundResourceFlowable<List<Repo>, List<RepoEntity>>() {
            override fun onFetchFailed() = repoListRateLimit.reset(owner)

            override fun saveCallResult(request: List<RepoEntity>) = repoDao.insertRepos(request)

            override fun shouldFetch(result: List<Repo>?) = result == null || result.isEmpty() || repoListRateLimit.shouldFetch(owner)

            override fun loadFromDb() = repoDao.loadRepositories(owner).map { it -> it.map { repoEntity -> repoEntity.toRepo() } }

            override fun createCall() = githubService.getRepos(owner)
        }.asFlowable()
    }

    override fun loadRepo(owner: String, name: String): Flowable<Resource<List<Repo>>> {
        return object : NetworkBoundResourceFlowable<List<Repo>, RepoEntity>() {
            override fun onFetchFailed() = repoRateLimit.reset("$owner/$name")

            override fun saveCallResult(request: RepoEntity) = repoDao.insert(request)

            override fun shouldFetch(result: List<Repo>?) = result == null || result.isEmpty() || repoRateLimit.shouldFetch("$owner/$name")

            override fun loadFromDb() = repoDao.load(ownerLogin = owner, name = name).map { it -> it.map { repoEntity -> repoEntity.toRepo() } }

            override fun createCall() = githubService.getRepo(owner = owner, name = name)
        }.asFlowable()
    }

    override fun loadContributors(owner: String, name: String): Flowable<Resource<List<Contributor>>> {
        return object : NetworkBoundResourceFlowable<List<Contributor>, List<ContributorEntity>>() {
            override fun onFetchFailed() = contributorListRateLimit.reset("$owner/$name")

            override fun saveCallResult(request: List<ContributorEntity>) {
                request.forEach {
                    it.repoName = name
                    it.repoOwner = owner
                }
                githubDb.runInTransaction {
                    repoDao.createRepoIfNotExists(
                        RepoEntity(
                            id = RepoEntity.UNKNOWN_ID,
                            name = name,
                            fullName = "$owner/$name",
                            description = "",
                            ownerEntity = RepoEntity.OwnerEntity(owner, null),
                            stars = 0
                        )
                    )
                    repoDao.insertContributors(request)
                }
            }

            override fun shouldFetch(result: List<Contributor>?) =
                result == null || result.isEmpty() || contributorListRateLimit.shouldFetch("$owner/$name")

            override fun loadFromDb() = repoDao.loadContributors(owner, name)
                .map { it -> it.map { contributorEntity -> contributorEntity.toContributor() } }

            override fun createCall() = githubService.getContributors(owner, name)
        }.asFlowable()
    }

    override fun search(query: String): Flowable<Resource<List<Repo>>> {
        return object : NetworkBoundResourceFlowable<List<Repo>, RepoSearchResponse>() {
            override fun onFetchFailed() = searchListRateLimit.reset(query)

            override fun saveCallResult(request: RepoSearchResponse) {
                val repoIds = request.items.map { it.id }
                val repoSearchResult = RepoSearchResultEntity(
                    query = query,
                    repoIds = repoIds,
                    totalCount = request.total,
                    next = request.nextPage
                )
                githubDb.runInTransaction {
                    repoDao.insertRepos(request.items)
                    repoDao.insert(repoSearchResult)
                }
            }

            override fun shouldFetch(result: List<Repo>?) = result == null || result.isEmpty() || searchListRateLimit.shouldFetch(query)

            override fun loadFromDb(): Flowable<List<Repo>> =
                repoDao.search(query).switchMap {
                    if (it.isNotEmpty()) {
                        repoDao.loadOrdered(it[0].repoIds)
                    } else {
                        Flowable.just(Collections.emptyList())
                    }
                }.map {
                    if (it.isNotEmpty()) {
                        it.map { repoEntity -> repoEntity.toRepo() }
                    } else {
                        Collections.emptyList()
                    }
                }

            override fun createCall() = githubService.searchRepos(query)

            override fun processResponse(response: ApiSuccessResponse<RepoSearchResponse>): RepoSearchResponse {
                val body = response.body
                body.nextPage = response.nextPage
                return body
            }
        }.asFlowable()
    }

    override fun searchNextPage(query: String): Single<Resource<Boolean>> {
        return Single.defer {
            repoDao.findSearchResult(query)
                // Read from disk on Computation Scheduler
                .subscribeOn(Schedulers.computation())
                .flatMap<Resource<Boolean>> {
                    if (it.isNotEmpty()) {
                        val current = it[0]
                        searchNextPageRemote(query, current)
                    } else {
                        Single.just(Resource.Success(false))
                    }
                }
                .onErrorReturn { Resource.Failure(it.toAppFailure()) }
                // Read results in Android Main Thread (UI)
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    private fun searchNextPageRemote(query: String, current: RepoSearchResultEntity): Single<Resource<Boolean>> {
        return Single.defer {
            val nextPage = current.next
            if (nextPage != null) {
                githubService.searchRepos(query, nextPage)
                    // Request API on IO Scheduler
                    .subscribeOn(Schedulers.io())
                    // Read/Write to disk on Computation Scheduler
                    .observeOn(Schedulers.computation())
                    .map {
                        val response = ApiResponse.create(it)
                        when (response) {
                            is ApiSuccessResponse -> {
                                val body = response.body
                                body.nextPage = response.nextPage
                                saveSearchNextPageResult(query, current, response.body)
                                Resource.Success(true)
                            }
                            is ApiEmptyResponse -> Resource.Success(false)
                            is ApiErrorResponse -> throw response.throwable
                        }
                    }
            } else {
                Single.just(Resource.Success(false))
            }
        }
    }

    private fun saveSearchNextPageResult(query: String, current: RepoSearchResultEntity, body: RepoSearchResponse) {
        val ids = arrayListOf<Int>()
        ids.addAll(current.repoIds)
        ids.addAll(body.items.map { it.id })
        val merged = RepoSearchResultEntity(
            query = query,
            repoIds = ids,
            totalCount = body.total,
            next = body.nextPage
        )

        githubDb.runInTransaction {
            githubDb.repoDao().insert(merged)
            githubDb.repoDao().insertRepos(body.items)
        }
    }
}
