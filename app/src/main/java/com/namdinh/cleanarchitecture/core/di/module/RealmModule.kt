package com.namdinh.cleanarchitecture.core.di.module

import android.app.Application
import com.namdinh.cleanarchitecture.core.di.scope.PerApplication
import com.namdinh.cleanarchitecture.data.db.realm.helper.RealmModules
import dagger.Module
import dagger.Provides
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Named

@Module
class RealmModule(application: Application) {
    init {
        Realm.init(application)
    }

    @Provides
    @PerApplication
    @Named("realm_name")
    internal fun provideRealmName(): String {
        return "clean_architecture_data"
    }

    @Provides
    @PerApplication
    @Named("realm_schema_version")
    internal fun provideRealmSchemaVersion(): Int {
        return 1
    }

    @Provides
    @PerApplication
    internal fun provideRealmModules(): RealmModules {
        return RealmModules()
    }

    @Provides
    @PerApplication
    internal fun provideRealmConfiguration(@Named("realm_name") realmName: String, @Named("realm_schema_version") realmSchemaVersion: Int, realmModules: RealmModules): RealmConfiguration {
        return RealmConfiguration.Builder()
                .name(realmName)
                .schemaVersion(realmSchemaVersion.toLong())
                .modules(realmModules)
                .deleteRealmIfMigrationNeeded()
                .build()
    }

    @Provides
    internal fun provideRealm(realmConfiguration: RealmConfiguration): Realm {
        return Realm.getInstance(realmConfiguration)
    }
}
