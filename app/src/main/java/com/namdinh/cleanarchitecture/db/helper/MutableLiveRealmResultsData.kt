package com.namdinh.cleanarchitecture.db.helper

import androidx.lifecycle.MutableLiveData
import io.realm.RealmChangeListener
import io.realm.RealmModel
import io.realm.RealmResults

class MutableLiveRealmResultsData<T : RealmModel>(private val results: RealmResults<T>) : MutableLiveData<RealmResults<T>>() {

    private val listener = RealmChangeListener<RealmResults<T>> { results -> postValue(results) }

    override fun onActive() {
        results.addChangeListener(listener)
    }

    override fun onInactive() {
        results.removeChangeListener(listener)
    }
}


