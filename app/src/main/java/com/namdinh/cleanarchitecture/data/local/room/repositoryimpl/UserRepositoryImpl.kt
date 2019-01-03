package com.namdinh.cleanarchitecture.data.local.room.repositoryimpl

import com.namdinh.cleanarchitecture.core.helper.AppExecutors
import com.namdinh.cleanarchitecture.core.helper.RateLimiter
import com.namdinh.cleanarchitecture.data.local.room.GithubDb
import com.namdinh.cleanarchitecture.data.local.room.dao.UserDao
import com.namdinh.cleanarchitecture.data.local.room.entity.UserEntity
import com.namdinh.cleanarchitecture.data.remote.GithubService
import com.namdinh.cleanarchitecture.data.remote.helper.rx.NetworkBoundResourceFlowable
import com.namdinh.cleanarchitecture.data.remote.helper.rx.Resource
import com.namdinh.cleanarchitecture.domain.repository.UserRepository
import com.namdinh.cleanarchitecture.domain.vo.User
import io.reactivex.Flowable
import retrofit2.Response
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    appExecutors: AppExecutors,
    githubDb: GithubDb,
    githubService: GithubService,
    private val userDao: UserDao
) : BaseRepositoryImpl(appExecutors, githubDb, githubService), UserRepository {

    companion object {
        private const val timeout = 30
    }

    private val userRateLimit = RateLimiter<String>(timeout, TimeUnit.MINUTES)

    override fun loadUser(login: String): Flowable<Resource<List<User>>> {
        return object : NetworkBoundResourceFlowable<List<User>, UserEntity>() {
            override fun onFetchFailed() {
                userRateLimit.reset(login)
            }

            override fun saveCallResult(request: UserEntity) {
                userDao.insert(request)
            }

            override fun shouldFetch(result: List<User>?): Boolean {
                return result == null || result.isEmpty() || userRateLimit.shouldFetch(login)
            }

            override fun loadFromDb(): Flowable<List<User>> {
                return userDao.findByLogin(login).map { userEntities -> userEntities.map { it.toUser() } }
            }

            override fun createCall(): Flowable<Response<UserEntity>> {
                return githubService.getUser(login)
            }
        }.asFlowable()
    }
}
