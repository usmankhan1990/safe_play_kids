package com.safeplay.kids

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SafePlayApp : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private lateinit var instance: SafePlayApp
        val context: Context get() = instance.applicationContext
    }
}