package com.trackier.example_app_kotlin

import android.app.Application
import android.content.Context
import com.trackier.sdk.AttributionParams
import com.trackier.sdk.DeepLink
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

//        val sdkConfig = TrackierSDKConfig(this, TR_DEV_KEY, "production")
//        val apkAttributes = AttributionParams("kFyW2bEizc", subSiteID= "sub_partner_tiktok", siteId = "google")
//        sdkConfig.setAttributionParams(apkAttributes)
//        TrackierSDK.initialize(sdkConfig)

        val sdkConfig = TrackierSDKConfig(this, TR_DEV_KEY, "development")
        val apkAttributes = AttributionParams("kFyW2bEizc", subSiteID= "sub_partner_tiktok", siteId = "google")
        sdkConfig.setAttributionParams(apkAttributes)
//        sdkConfig.setManualMode(true)
//        TrackierSDK.setLocalRefTrack(true,"_")
        TrackierSDK.setUserId("pppppp")
        TrackierSDK.setUserEmail("abc@gmail.com")
        val userAdditionalDetails: MutableMap<String,Any> = mutableMapOf()
        userAdditionalDetails.put("userMobile",9999000000)
        TrackierSDK.setUserAdditionalDetails(userAdditionalDetails)
        TrackierSDK.initialize(sdkConfig)
    }
}
