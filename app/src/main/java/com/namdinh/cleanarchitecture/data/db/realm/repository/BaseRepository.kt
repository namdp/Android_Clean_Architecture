package com.namdinh.cleanarchitecture.data.db.realm.repository

import com.namdinh.cleanarchitecture.core.helper.AppExecutors
import com.namdinh.cleanarchitecture.data.network.GithubService
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmModel

abstract class BaseRepository<T : RealmModel>(protected var appExecutors: AppExecutors,
                                              protected var githubService: GithubService,
                                              protected var realmConfiguration: RealmConfiguration) {

    protected var realm: Realm = Realm.getInstance(realmConfiguration)

    fun onCleared() {
        realm.close()
    }
}
