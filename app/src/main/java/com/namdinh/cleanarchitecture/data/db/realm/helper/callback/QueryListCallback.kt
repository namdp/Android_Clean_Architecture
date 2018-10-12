package com.namdinh.cleanarchitecture.data.db.realm.helper.callback

import io.realm.RealmModel
import io.realm.RealmQuery
import io.realm.RealmResults

interface QueryListCallback<T : RealmModel> {
    fun onQueryCompleted(realmObjects: RealmResults<T>?)

    fun onQuery(realmQuery: RealmQuery<T>): RealmResults<T>?
}
