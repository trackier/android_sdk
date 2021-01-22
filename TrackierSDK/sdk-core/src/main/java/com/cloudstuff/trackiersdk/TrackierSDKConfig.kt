package com.cloudstuff.trackiersdk

import android.content.Context
import java.util.logging.Level

class TrackierSDKConfig(var context: Context, var appToken: String, val env: String) {
    private var enableApkTracking = false
    private val logger = Factory.logger

    init {
        context = context.applicationContext
        setLogLevel(if (env == Constants.ENV_PRODUCTION) Level.SEVERE else Level.FINEST)
        appToken = appToken //change by prak24
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