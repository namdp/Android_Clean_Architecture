package com.namdinh.cleanarchitecture.presentation.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.namdinh.cleanarchitecture.presentation.main.MainActivity

/*
 * Splash Screen -> The right way
 * https://android.jlelse.eu/right-way-to-create-splash-screen-on-android-e7f1709ba154
 */

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
