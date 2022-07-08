/*
 * Created by Muhamad Syafii
 * Thursday, 27/1/2022
 * Copyright (c) 2022 by Gibox Digital Asia.
 * All Rights Reserve
 */

package com.gibox.baseprojectmvvmandroid.view.ui.splashscreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.gibox.baseprojectmvvmandroid.R
import com.gibox.baseprojectmvvmandroid.util.openActivity
import com.gibox.baseprojectmvvmandroid.view.ui.home.MainActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
@ExperimentalCoroutinesApi
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            openActivity(MainActivity::class.java)
            finish()
        }, 1500L)
    }
}