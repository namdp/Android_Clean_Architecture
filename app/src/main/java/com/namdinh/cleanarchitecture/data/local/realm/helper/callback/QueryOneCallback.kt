package com.namdinh.cleanarchitecture.data.local.realm.helper.callback

import io.realm.RealmModel
import io.realm.RealmQuery

interface QueryOneCallback<T : RealmModel> {
    fun onQueryCompleted(realmObject: T?)

    fun onQuery(realmQuery: RealmQuery<T>): T?
}
