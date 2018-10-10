@file:JvmName("RealmUtils")

package com.namdinh.cleanarchitecture.db.helper

import io.realm.RealmModel
import io.realm.RealmResults

fun <T : RealmModel> T.asLiveData() = LiveRealmData(this)
fun <T : RealmModel> T.asMutableLiveData() = MutableLiveRealmData<T>(this)

fun <T : RealmModel> RealmResults<T>.asLiveData() = LiveRealmResultsData<T>(this)
fun <T : RealmModel> RealmResults<T>.asMutableLiveData() = MutableLiveRealmResultsData<T>(this)