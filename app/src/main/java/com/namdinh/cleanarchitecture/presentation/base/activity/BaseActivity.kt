package com.namdinh.cleanarchitecture.presentation.base.activity

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.android.support.HasSupportFragmentInjector

abstract class BaseActivity : AppCompatActivity(), HasSupportFragmentInjector {
    protected lateinit var navController: NavController

    @get:LayoutRes
    protected abstract val layoutId: Int

    @get:IdRes
    protected abstract val navControllerId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        setupNavigation()
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    private fun setupNavigation() {
        navController = findNavController(navControllerId)
    }
}