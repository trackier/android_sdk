package com.trackier.sdk

import android.content.Context
import java.util.logging.Level
import java.util.logging.Logger

class TrackierSDKConfig(var context: Context, val appToken: String, val env: String) {
    private val logger: Logger
    private var attributionParams: AttributionParams? = null
    private var deepLinkListener: DeepLinkListener? = null
    private var sdkt: String = "android"
    private var sdkVersion: String = Constants.SDK_VERSION
    private var minSessionTime: Int = 10
    private var manualTracking = false
    private var disableOrganicTrack = false
    private var secretId: String = ""
    private var secretKey: String = ""
    private var androidId: String = ""
    private var region: String = ""
    private var facebookAppId: String = ""

    init {
        context = context.applicationContext
        val level = if (env == Constants.ENV_PRODUCTION) Level.SEVERE else Level.FINEST
        Factory.setLogLevel(level)
        logger = Factory.logger
        Factory.setConfig(this)
    }

    fun setAppSecret(secretId: String, secretKey: String) {
        this.secretId = secretId
        this.secretKey = secretKey
    }

    fun getAppSecretId(): String {
        return this.secretId
    }

    fun getAppSecretKey(): String {
        return this.secretKey
    }

    /**
     * Set Facebook App ID for Meta Install Referrer support
     * This enables collection of install referrer data from Facebook, Instagram, and Facebook Lite apps
     * @param facebookAppId Your Facebook App ID from Meta for Developers
     */
    fun setFacebookAppId(facebookAppId: String) {
        this.facebookAppId = facebookAppId
    }

    /**
     * Get the configured Facebook App ID
     * @return Facebook App ID if configured, empty string otherwise
     */
    fun getFacebookAppId(): String {
        return this.facebookAppId
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
        this.sdkt = sdtk
    }

    fun getSDKType(): String {
        return this.sdkt
    }

    fun getSDKVersion(): String {
        return this.sdkVersion
    }

    fun setSDKVersion(sdkVersion: String) {
        this.sdkVersion = sdkVersion
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

    fun setManualMode(value: Boolean) {
        this.manualTracking = value
    }

    fun getManualMode(): Boolean {
        return this.manualTracking
    }

    fun disableOrganicTracking(value: Boolean) {
        this.disableOrganicTrack = value
    }

    fun getOrganicTracking(): Boolean {
        return this.disableOrganicTrack
    }

    fun setAndroidId(value: String) {
        this.androidId = value
    }

    fun getAndroidId(): String {
        return this.androidId
    }

    enum class Region {
        IN, GLOBAL
    }

    fun setRegion(value: Region) {
        this.region = value.name.lowercase()
    }

    fun getRegion(): String {
        return this.region
    }
}
