package com.namdinh.cleanarchitecture.data.local.realm.helper

import androidx.lifecycle.LiveData

import io.realm.RealmChangeListener
import io.realm.RealmModel
import io.realm.RealmObject

/**
 * Class connecting the Realm lifecycle to that of LiveData objects.
 */
class LiveRealmData<T : RealmModel>(private val result: T) : LiveData<T>() {

    private val listener = RealmChangeListener<T> { value -> postValue(value) }

    init {
        postValue(result)
    }

    override fun onActive() {
        RealmObject.isValid(result)
        RealmObject.addChangeListener(result, listener)
    }

    override fun onInactive() {
        RealmObject.removeChangeListener(result, listener)
    }
}


