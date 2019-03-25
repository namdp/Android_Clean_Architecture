package com.namdinh.cleanarchitecture.presentation.main

import android.os.Bundle
import com.namdinh.cleanarchitecture.R
import com.namdinh.cleanarchitecture.presentation.base.activity.BaseActivity

class MainActivity : BaseActivity() {

    override val layoutId: Int
        get() = R.layout.main_activity

    override val navControllerId: Int
        get() = R.id.nav_host_fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Test trigger 1
        //Test trigger 2
    }
}
