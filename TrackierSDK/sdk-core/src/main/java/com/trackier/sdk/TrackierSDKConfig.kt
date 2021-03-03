package com.trackier.sdk

import android.content.Context
import java.util.logging.Logger
import java.util.logging.Level

class TrackierSDKConfig(var context: Context, val appToken: String, val env: String) {
    private var enableApkTracking = false
    private val logger: Logger

    init {
        context = context.applicationContext
        val level = if (env == Constants.ENV_PRODUCTION) Level.SEVERE else Level.FINEST
        Factory.setLogLevel(level)
        logger = Factory.logger
    }


    fun setLogLevel(value: Level) {
        Factory.setLogLevel(value)
    }

    fun setApkTracking(value: Boolean) {
        enableApkTracking = value
    }

    fun isApkTrackingEnabled(): Boolean {
        return enableApkTracking
    }
}