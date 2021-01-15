package com.cloudstuff.trackiersdk

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.Exception

class TrackierSDKInstance {
    private val device = DeviceInfo()
    private val logger = Factory.logger
    var isEnabled = true
    var isInitialized = false
    var configLoaded = false
    var gaid: String? = null
    var isLAT = false
    lateinit var config: TrackierSDKConfig

    /**
     * Initialize method should be called to initialize the sdk
     */
    fun initialize(config: TrackierSDKConfig) {
        if (configLoaded) {
            return
        }
        this.config = config
        this.configLoaded = true
        DeviceInfo.init(device, this.config.context)
        CoroutineScope(Dispatchers.IO).launch {
            initGaid()
            initAttributionInfo()
            trackInstall()
        }
    }

    private suspend fun initGaid() {
        val (gaid, isLat) = DeviceInfo.getGAID(this.config.context)
        this.gaid = gaid
        this.isLAT = isLat
    }

    private suspend fun initAttributionInfo() {
        isInitialized = true
    }

    private fun isReferrerStored(): Boolean {
        val url = Util.getSharedPrefString(this.config.context, Constants.SHARED_PREF_INSTALL_URL)
        return url.isNotBlank()
    }

    private fun getReferrerDetails(): RefererDetails {
        var url = Util.getSharedPrefString(this.config.context, Constants.SHARED_PREF_INSTALL_URL)
        val clickTime = Util.getSharedPrefString(this.config.context,Constants.SHARED_PREF_CLICK_TIME)
        val installTime = Util.getSharedPrefString(this.config.context,Constants.SHARED_PREF_INSTALL_TIME)
        if (url.isBlank()) {
            url = RefererDetails.ORGANIC_REF
        }
        return RefererDetails(url, clickTime, installTime)
    }

    private fun setReferrerDetails(refererDetails: RefererDetails) {
        val prefs = Util.getSharedPref(this.config.context)
        prefs.edit().putString(Constants.SHARED_PREF_INSTALL_URL, refererDetails.url)
            .putString(Constants.SHARED_PREF_CLICK_TIME, refererDetails.clickTime)
            .putString(Constants.SHARED_PREF_INSTALL_TIME, refererDetails.installTime)
            .apply()
    }

    private fun makeWorkRequest(kind: String): TrackierWorkRequest {
        val trackierWorkRequest = TrackierWorkRequest(kind)
        trackierWorkRequest.device = device
        trackierWorkRequest.gaid = gaid
        trackierWorkRequest.refDetails = getReferrerDetails()
        return trackierWorkRequest
    }

    private fun isInstallTracked(): Boolean {
        return try {
            val prefs = Util.getSharedPref(this.config.context)
            prefs.getBoolean(Constants.SHARED_PREF_IS_INSTALL_TRACKED, false)
        } catch (ex: Exception) {
            false
        }
    }

    private fun setInstallTracked() {
        val prefs = Util.getSharedPref(this.config.context)
        prefs.edit().putBoolean(Constants.SHARED_PREF_IS_INSTALL_TRACKED, true).apply()
    }

    private suspend fun trackInstall() {
        if (isInstallTracked()) {
            return
        }
        if (!isReferrerStored()) {
            val installRef = InstallReferrer(this.config.context)
            val refDetails = installRef.getRefDetails()
            this.setReferrerDetails(refDetails)
        }
        val wrkRequest = makeWorkRequest(TrackierWorkRequest.KIND_INSTALL)
        TrackierWorkRequest.enqueue(wrkRequest)
        setInstallTracked()
    }

    fun trackEvent(event: TrackierEvent) {
        if (!isEnabled || !configLoaded) {
            return
        }
        if (!isInstallTracked()) {
            logger.warning("Event Tracking request sent before install was tracked")
        }
        if (!isInitialized) {
            logger.warning("Event Tracking request sent before SDK data was initialized")
        }
        val wrkRequest = makeWorkRequest(TrackierWorkRequest.KIND_EVENT)
        wrkRequest.event = event
        TrackierWorkRequest.enqueue(wrkRequest)
    }
}