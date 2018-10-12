package com.namdinh.cleanarchitecture.data.db.realm.helper

import androidx.lifecycle.LiveData

import io.realm.RealmChangeListener
import io.realm.RealmModel
import io.realm.RealmResults

/**
 * Class connecting the Realm lifecycle to that of LiveData objects.
 */
class LiveRealmResultsData<T : RealmModel>(private val results: RealmResults<T>) : LiveData<RealmResults<T>>() {

    private val listener = RealmChangeListener<RealmResults<T>> { value -> postValue(value) }

    init {
        postValue(results)
    }

    override fun onActive() {
        results.addChangeListener(listener)
    }

    override fun onInactive() {
        results.removeChangeListener(listener)
    }

}


