package com.namdinh.cleanarchitecture.data.db.realm.helper

import com.namdinh.cleanarchitecture.data.db.realm.helper.callback.QueryListCallback
import com.namdinh.cleanarchitecture.data.db.realm.helper.callback.QueryOneCallback
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmResults
import javax.inject.Inject

class RealmHelper @Inject
constructor() {

    fun <T : RealmModel> insertOrUpdate(realm: Realm, realmObject: T?) {
        if (realmObject != null) {
            realm.executeTransaction { realm1 -> realm1.insertOrUpdate(realmObject) }
        }
    }

    fun <T : RealmModel> insertOrUpdateAsync(realm: Realm, realmObject: T?) {
        if (realmObject != null) {
            realm.executeTransactionAsync { realm1 -> realm1.insertOrUpdate(realmObject) }
        }
    }

    fun <T : RealmModel> insertOrUpdate(realm: Realm, objects: List<T>?) {
        if (objects?.isNotEmpty() == true)
            realm.executeTransaction { realm1 -> realm1.insertOrUpdate(objects) }
    }

    fun <T : RealmModel> insertOrUpdateAsync(realm: Realm, objects: List<T>?) {
        if (objects?.isNotEmpty() == true)
            realm.executeTransactionAsync { realm1 -> realm1.insertOrUpdate(objects) }
    }

    fun <T : RealmModel> delete(realm: Realm, clazz: Class<T>) {
        realm.executeTransaction { realm1 -> realm1.where(clazz).findAll().deleteAllFromRealm() }
    }

    fun <T : RealmModel> deleteAsync(realm: Realm, clazz: Class<T>) {
        realm.executeTransactionAsync { realm1 -> realm1.where(clazz).findAll().deleteAllFromRealm() }
    }

    fun <T : RealmModel> delete(realm: Realm, clazz: Class<T>, callback: QueryListCallback<T>) {
        realm.executeTransaction { realm1 ->
            val realmResults = queryList(realm1, clazz, callback)
            if (realmResults?.isNotEmpty() == true) {
                realmResults.deleteAllFromRealm()
            }
        }
    }

    fun <T : RealmModel> deleteAsync(realm: Realm, clazz: Class<T>, callback: QueryListCallback<T>) {
        realm.executeTransactionAsync { realm1 ->
            val realmResults = queryList(realm1, clazz, callback)
            if (realmResults?.isNotEmpty() == true) {
                realmResults.deleteAllFromRealm()
            }
        }
    }

    fun <T : RealmModel> findFirst(realm: Realm, clazz: Class<T>): T? {
        val query = realm.where(clazz)
        return query.findFirst()
    }

    fun <T : RealmModel> findAll(realm: Realm, clazz: Class<T>): RealmResults<T>? {
        val query = realm.where(clazz)
        return query.findAll()
    }

    fun <T : RealmModel> queryOne(realm: Realm, clazz: Class<T>, callback: QueryOneCallback<T>): T? {
        val query = realm.where(clazz)
        return callback.onQuery(query)
    }

    fun <T : RealmModel> queryList(realm: Realm, clazz: Class<T>, callback: QueryListCallback<T>): RealmResults<T>? {
        val query = realm.where(clazz)
        return callback.onQuery(query)
    }
}
