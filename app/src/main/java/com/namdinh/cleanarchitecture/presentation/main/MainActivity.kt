package com.namdinh.cleanarchitecture.presentation.main

import com.namdinh.cleanarchitecture.R
import com.namdinh.cleanarchitecture.presentation.base.activity.BaseActivity

class MainActivity : BaseActivity() {

    override val layoutId: Int
        get() = R.layout.activity_main

    override val navControllerId: Int
        get() = R.id.nav_host_fragment
}
