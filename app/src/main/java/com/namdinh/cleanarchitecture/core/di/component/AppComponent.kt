package com.namdinh.cleanarchitecture.core.di.component

import android.app.Application
import com.namdinh.cleanarchitecture.CleanArchitectureApp
import com.namdinh.cleanarchitecture.core.di.module.AppModule
import com.namdinh.cleanarchitecture.core.di.module.NetworkModule
import com.namdinh.cleanarchitecture.core.di.module.RealmModule
import com.namdinh.cleanarchitecture.core.di.module.RoomModule
import com.namdinh.cleanarchitecture.core.di.module.builder.ActivityBuildersModule
import com.namdinh.cleanarchitecture.core.di.module.builder.FragmentBuildersModule
import com.namdinh.cleanarchitecture.core.di.module.builder.RepositoryBuildersModule
import com.namdinh.cleanarchitecture.core.di.qualifier.ServerAddress
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AppModule::class,
        NetworkModule::class,
        RoomModule::class,
        RealmModule::class,
        RepositoryBuildersModule::class,
        ActivityBuildersModule::class,
        FragmentBuildersModule::class]
)
interface AppComponent {

    @Component.Builder
    interface Builder {

        // bind application to all Module
        @BindsInstance
        fun application(application: Application): Builder

        // bind server address String to NetworkModule
        @BindsInstance
        fun serverAddress(@ServerAddress serverAddress: String): Builder

        // bind Realm module manually
        fun realmModule(realmModule: RealmModule): Builder

        fun build(): AppComponent
    }

    fun inject(cleanArchitectureApp: CleanArchitectureApp)
}
