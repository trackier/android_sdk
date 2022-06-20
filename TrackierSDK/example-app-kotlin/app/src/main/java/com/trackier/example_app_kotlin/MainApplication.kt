package com.trackier.example_app_kotlin

import android.app.Application
import android.content.Context
import com.trackier.sdk.*

class MainApplication : Application() {
    init {
        instance = this
    }

    companion object {
        private var instance: MainApplication? = null

        fun applicationContext(): Context {
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
        sdkConfig.setDeepLinkListener(deepLinkListener)


        val apkAttributes =
            AttributionParams("kFyW2bEizc", subSiteID = "sub_partner_tiktok", siteId = "google")
        sdkConfig.setAttributionParams(apkAttributes)
//        sdkConfig.setManualMode(true)
//        TrackierSDK.setLocalRefTrack(true,"_")

        /* pass true argument to disable orgainic track from sdk. */
//        sdkConfig.disableOrganicTracking(true)
        TrackierSDK.setUserId("pppppp")
        TrackierSDK.setUserEmail("abc@gmail.com")
        val userAdditionalDetails: MutableMap<String, Any> = mutableMapOf()
        userAdditionalDetails.put("userMobile", 9999000000)
        TrackierSDK.setUserAdditionalDetails(userAdditionalDetails)
        TrackierSDK.initialize(sdkConfig)
    }
}

object deepLinkListener : DeepLinkListener {
    override fun onDeepLinking(result: DeepLink) {
        // we have deepLink object and we can get any valve from Object

    }
}
