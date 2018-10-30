package com.namdinh.cleanarchitecture.core.di.component

import android.app.Application
import com.namdinh.cleanarchitecture.CleanArchitectureApp
import com.namdinh.cleanarchitecture.core.di.module.*
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
            RealmModule::class,
            ActivityBuildersModule::class,
            FragmentBuildersModule::class,
            RepositoryBuildersModule::class]
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
