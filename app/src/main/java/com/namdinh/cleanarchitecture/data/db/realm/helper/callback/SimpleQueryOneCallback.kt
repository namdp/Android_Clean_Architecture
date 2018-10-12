package com.namdinh.cleanarchitecture.data.db.realm.helper.callback

import io.realm.RealmModel
import io.realm.RealmQuery

open class SimpleQueryOneCallback<T : RealmModel> : QueryOneCallback<T> {

    override fun onQueryCompleted(realmObject: T?) {

    }

    override fun onQuery(realmQuery: RealmQuery<T>): T? {
        return null
    }
}
