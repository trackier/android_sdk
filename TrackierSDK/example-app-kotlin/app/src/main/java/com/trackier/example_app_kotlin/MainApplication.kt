package com.trackier.example_app_kotlin

import android.app.Application
import android.content.Context
import com.trackier.sdk.APKAttributes
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

         val TR_DEV_KEY: String = "xxxx-xx-4505-bc8b-xx"

        // Use ApplicationContext.
        // example: SharedPreferences etc...
        val context: Context = MainApplication.applicationContext()

        val sdkConfig = TrackierSDKConfig(this, TR_DEV_KEY, "production")
//        val apkAttributes = APKAttributes("p1d","si23","ssite122","cha12","ad111","adid23")
        val apkAttributes = APKAttributes("demo_partner", subSiteID= "sub_partner_tiktok", siteId = "google")

        sdkConfig.setAPKAttributes(apkAttributes)
        
        TrackierSDK.initialize(sdkConfig)
    }
}
