package com.namdinh.cleanarchitecture.data.local.realm.repository

interface UserRepository {
//    fun movies(): Either<Failure, List<Movie>>
//
//
//    //Implementation
//    class Data @Inject constructor(appExecutors: AppExecutors,
//                                   githubService: GithubService,
//                                   realmConfiguration: RealmConfiguration,
//                                   val userDao: UserDao
//    ) : BaseRepositoryImpl<UserEntity>(appExecutors, githubService, realmConfiguration), UserRepositoryImpl {
//
//        private val repoListRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)
//
//
//        override fun movies(): Either<Failure, List<Movie>>
//
//        fun loadUser(login: String): Flowable<Resource<UserEntity>> {
//            return object : NetworkBoundResourceFlowable<UserEntity, UserEntity>() {
//                override fun saveCallResult(item: UserEntity) {
//                    userDao.insertOrUpdate(realm, item)
//                }
//
//                override fun shouldFetch(data: UserEntity?) = data == null || repoListRateLimit.shouldFetch(login)
//
//
//                override fun loadFromDb() = userDao.findUser(realm, login)
//
//                override fun createCall() = githubService.getUser(login)
//            }.asFlowable()
//        }
//
//    }
}
