/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.namdinh.cleanarchitecture.di.component

import android.app.Application
import com.namdinh.cleanarchitecture.CleanArchitectureApp
import com.namdinh.cleanarchitecture.di.module.AppModule
import com.namdinh.cleanarchitecture.di.module.NetworkModule
import com.namdinh.cleanarchitecture.di.module.RealmModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(
        modules = [
            AndroidInjectionModule::class,
            AppModule::class,
            NetworkModule::class,
            RealmModule::class]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        // bind server address String to NetworkModule
        @BindsInstance
        @Named("server_address")
        fun serverAddress(serverAddress: String): Builder

        // bind Realm module manually
        fun realmModule(realmModule: RealmModule): Builder

        fun build(): AppComponent
    }

    fun inject(cleanArchitectureApp: CleanArchitectureApp)
}
