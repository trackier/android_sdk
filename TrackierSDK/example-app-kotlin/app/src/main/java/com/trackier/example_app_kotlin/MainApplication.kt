package com.trackier.example_app_kotlin

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
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
         val TR_DEV_KEY: String = "693927f2-fa54-44db-b31e-3f992a86a897"
        // Use ApplicationContext.
        // example: SharedPreferences etc...
        val context: Context = MainApplication.applicationContext()
        val sdkConfig = TrackierSDKConfig(this, TR_DEV_KEY, "development")
        val apkAttributes = APKAttributes(parterId = "s-0aV7kleA",siteId = "si23",subSiteID = "ssite122",channel = "cha12",ad = "ad111",adId = "adid23")
        sdkConfig.setSDKType()
        sdkConfig.setAPKAttributes(apkAttributes)
        TrackierSDK.initialize(sdkConfig)

        val deeplinkEvent = DeeplinkEvent()
        deeplinkEvent.setDeeplinkListener(object : DeeplinkEvent.DeeplinkListner{
            override fun receivedDeeplink(deeplink: Uri) {
                Log.d("TAG", "receivedDeeplink: "+ deeplink)
            }
        })

    }
}