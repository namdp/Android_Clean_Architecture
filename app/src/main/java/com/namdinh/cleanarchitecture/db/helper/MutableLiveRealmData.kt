package com.namdinh.cleanarchitecture.db.helper

import androidx.lifecycle.MutableLiveData
import io.realm.RealmChangeListener
import io.realm.RealmModel
import io.realm.RealmObject

class MutableLiveRealmData<T : RealmModel>(private val result: T) : MutableLiveData<T>() {

    private val listener = RealmChangeListener<T> { value -> postValue(value) }

    override fun onActive() {
        RealmObject.addChangeListener(result, listener)
    }

    override fun onInactive() {
        RealmObject.removeChangeListener(result, listener)
    }
}


