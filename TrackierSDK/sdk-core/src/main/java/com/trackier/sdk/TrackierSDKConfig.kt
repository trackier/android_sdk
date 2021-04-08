package com.trackier.sdk

import android.content.Context
import java.util.logging.Logger
import java.util.logging.Level

class TrackierSDKConfig(var context: Context, val appToken: String, val env: String) {
    private var enableApkTracking = false
    private val logger: Logger
    private var apkAttributes: APKAttributes? = null
    private var sdtk: String = "android"

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

    fun setAPKAttributes(apkAttributes: APKAttributes){
        this.apkAttributes = apkAttributes
    }

    fun getAPKAttributes(): APKAttributes? {
        return this.apkAttributes
    }

    fun setSDKType(sdtk: String = "android"){
        this.sdtk = sdtk
    }

    fun getSDKType(): String{
        return this.sdtk
    }
}