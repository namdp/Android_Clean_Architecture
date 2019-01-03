package com.namdinh.cleanarchitecture.core.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.namdinh.cleanarchitecture.BuildConfig
import com.namdinh.cleanarchitecture.CleanArchitectureApp
import com.namdinh.cleanarchitecture.core.di.component.DaggerAppComponent
import com.namdinh.cleanarchitecture.core.di.component.DaggerBindingComponent
import com.namdinh.cleanarchitecture.core.di.helper.Injectable
import com.namdinh.cleanarchitecture.core.di.module.RealmModule
import dagger.android.AndroidInjection
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector

/**
 * Helper class to automatically inject fragments if they implement [Injectable].
 */
object AppInjector {
    fun init(cleanArchitectureApp: CleanArchitectureApp) {
        val appComponent = DaggerAppComponent.builder().application(application = cleanArchitectureApp)
            .serverAddress(serverAddress = BuildConfig.SERVER_ADDRESS)
            .realmModule(RealmModule(application = cleanArchitectureApp)).build()
        appComponent.inject(cleanArchitectureApp)

        // data binding
        val bindingComponent = DaggerBindingComponent.builder().appComponent(appComponent).build()
        DataBindingUtil.setDefaultComponent(bindingComponent)

        cleanArchitectureApp
            .registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    handleActivity(activity)
                }

                override fun onActivityStarted(activity: Activity) {
                }

                override fun onActivityResumed(activity: Activity) {
                }

                override fun onActivityPaused(activity: Activity) {
                }

                override fun onActivityStopped(activity: Activity) {
                }

                override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {
                }

                override fun onActivityDestroyed(activity: Activity) {
                }
            })
    }

    private fun handleActivity(activity: Activity) {
        if (activity is HasSupportFragmentInjector) {
            AndroidInjection.inject(activity)
        }
        if (activity is FragmentActivity) {
            activity.supportFragmentManager
                .registerFragmentLifecycleCallbacks(
                    object : FragmentManager.FragmentLifecycleCallbacks() {
                        override fun onFragmentCreated(
                            fm: FragmentManager,
                            f: Fragment,
                            savedInstanceState: Bundle?
                        ) {
                            if (f is Injectable) {
                                AndroidSupportInjection.inject(f)
                            }
                        }
                    }, true
                )
        }
    }
}
