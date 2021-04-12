package com.trackier.sdk

import android.net.Uri
import androidx.annotation.Keep

@Keep
object TrackierSDK{
    private var isInitialized = false
    private val logger = Factory.logger
    private var instance = TrackierSDKInstance()


    @JvmStatic
    fun initialize(config: TrackierSDKConfig) {
        if (isInitialized) {
            logger.finest("SDK Already initialized")
            return
        }
        isInitialized = true
        logger.info("Trackier SDK ${Constants.SDK_VERSION} initialized")
        instance.initialize(config)
    }

    fun isEnabled(): Boolean {
        return instance.isEnabled
    }

    fun setEnabled(value: Boolean) {
        instance.isEnabled = value
    }

    @JvmStatic
    fun trackEvent(event: TrackierEvent) {
        if (!isInitialized) {
            logger.finest("SDK Not Initialized")
            return
        }
        if (!isEnabled()) {
            logger.finest("SDK Disabled")
            return
        }
        instance.trackEvent(event)
    }

    @JvmStatic
    suspend fun trackSession() {
        instance.trackSession()
    }

    @JvmStatic
     fun appWillOpenUrl(uri : Uri) {
         val deeplinkListner = this as DeeplinkEvent.DeeplinkListner
         deeplinkListner.receivedDeeplink(uri)
     }

}
