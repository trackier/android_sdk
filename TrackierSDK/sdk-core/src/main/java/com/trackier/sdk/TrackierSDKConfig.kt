package com.trackier.sdk

import android.content.Context
import com.cloudstuff.trackiersdk.Constants
import com.cloudstuff.trackiersdk.Factory
import java.util.logging.Level

class TrackierSDKConfig(var context: Context, val appToken: String, val env: String) {
    private var enableApkTracking = false
    private val logger = Factory.logger

    init {
        context = context.applicationContext
        setLogLevel(if (env == Constants.ENV_PRODUCTION) Level.SEVERE else Level.FINEST)
    }


    fun setLogLevel(value: Level) {
        logger.level = value
    }

    fun setApkTracking(value: Boolean) {
        enableApkTracking = value
    }

    fun isApkTrackingEnabled(): Boolean {
        return enableApkTracking
    }
}