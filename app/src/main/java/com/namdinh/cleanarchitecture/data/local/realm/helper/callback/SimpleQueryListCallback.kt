package com.namdinh.cleanarchitecture.data.local.realm.helper.callback

import io.realm.RealmModel
import io.realm.RealmQuery
import io.realm.RealmResults

open class SimpleQueryListCallback<T : RealmModel> : QueryListCallback<T> {

    override fun onQueryCompleted(realmObjects: RealmResults<T>?) {}

    override fun onQuery(realmQuery: RealmQuery<T>): RealmResults<T>? {
        return null
    }
}
