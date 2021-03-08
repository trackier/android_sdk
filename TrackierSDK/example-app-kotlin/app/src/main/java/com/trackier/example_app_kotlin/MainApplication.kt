package com.trackier.example_app_kotlin

import android.app.Application
import android.content.Context
import com.trackier.sdk.TrackierSDK
import com.trackier.sdk.TrackierSDKConfig

class MainApplication : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: MainApplication? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        // initialize for any

         val TR_DEV_KEY: String = "c814db62-c196-4505-bc8b-46fa8e37f688"

        // Use ApplicationContext.
        // example: SharedPreferences etc...
        val context: Context = MainApplication.applicationContext()

        val sdkConfig = TrackierSDKConfig(this, TR_DEV_KEY, "production")
        TrackierSDK.initialize(sdkConfig)
    }
}