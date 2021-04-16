package com.trackier.sdk

import android.content.Context
import java.util.logging.Logger
import java.util.logging.Level

class TrackierSDKConfig(var context: Context, val appToken: String, val env: String) {
    private val logger: Logger
    private var attributionParams: AttributionParams? = null
    private var deepLinkListener: DeepLinkListener? = null
    private var sdtk: String = "android"
    private var minSessionTime: Int = 10

    init {
        context = context.applicationContext
        val level = if (env == Constants.ENV_PRODUCTION) Level.SEVERE else Level.FINEST
        Factory.setLogLevel(level)
        logger = Factory.logger
    }


    fun setLogLevel(value: Level) {
        Factory.setLogLevel(value)
    }

    fun setAttributionParams(attributionParams: AttributionParams) {
        this.attributionParams = attributionParams
    }

    fun getAttributionParams(): AttributionParams? {
        return this.attributionParams
    }

    fun setSDKType(sdtk: String) {
        this.sdtk = sdtk
    }

    fun getSDKType(): String {
        return this.sdtk
    }

    fun setMinSessionDuration(value: Int) {
        if (value > 0) {
            this.minSessionTime = value
        }
    }

    fun getMinSessionDuration(): Int {
        return this.minSessionTime
    }

    fun setDeepLinkListener(lt: DeepLinkListener) {
        this.deepLinkListener = lt
    }

    fun getDeepLinkListener(): DeepLinkListener? {
        return this.deepLinkListener
    }
}