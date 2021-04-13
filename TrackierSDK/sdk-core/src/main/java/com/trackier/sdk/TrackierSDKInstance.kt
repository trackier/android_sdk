package com.trackier.sdk

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.Exception

class TrackierSDKInstance {
    private val device = DeviceInfo()
    lateinit var config: TrackierSDKConfig
    private var refDetails: RefererDetails? = null
    private var appToken: String = ""

    var isEnabled = true
    var isInitialized = false
    var configLoaded = false
    var gaid: String? = null
    var isLAT = false
    var installId = ""

    /**
     * Initialize method should be called to initialize the sdk
     */
    fun initialize(config: TrackierSDKConfig) {
        if (configLoaded) {
            return
        }
        this.config = config
        this.configLoaded = true
        this.appToken = this.config.appToken
        this.installId = getInstallID()
        DeviceInfo.init(device, this.config.context)
        CoroutineScope(Dispatchers.IO).launch {
            initGaid()
            initAttributionInfo()
            trackInstall()
            trackSession()
            // fire dl hook
//            config.getDeepLinkListener()?.onDeepLinking()
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
        if (refDetails != null) {
            return refDetails!!
        }
        var url = Util.getSharedPrefString(this.config.context, Constants.SHARED_PREF_INSTALL_URL)
        val clickTime = Util.getSharedPrefString(this.config.context, Constants.SHARED_PREF_CLICK_TIME)
        val installTime = Util.getSharedPrefString(this.config.context, Constants.SHARED_PREF_INSTALL_TIME)
        if (url.isBlank()) {
            url = RefererDetails.ORGANIC_REF
        }
        refDetails = RefererDetails(url, clickTime, installTime)
        return refDetails!!
    }

    private fun setReferrerDetails(refererDetails: RefererDetails) {
        refDetails = refererDetails
        val prefs = Util.getSharedPref(this.config.context)
        prefs.edit().putString(Constants.SHARED_PREF_INSTALL_URL, refererDetails.url)
            .putString(Constants.SHARED_PREF_CLICK_TIME, refererDetails.clickTime)
            .putString(Constants.SHARED_PREF_INSTALL_TIME, refererDetails.installTime)
            .apply()
    }

    private fun setInstallID(installID: String) {
            val prefs = Util.getSharedPref(this.config.context)
            prefs.edit().putString(Constants.SHARED_PREF_INSTALL_ID, installID)
                    .apply()
    }

    private fun getInstallID(): String {
        var installId = Util.getSharedPrefString(this.config.context, Constants.SHARED_PREF_INSTALL_ID)
        if(installId.isBlank()){
            installId = UUID.randomUUID().toString()
            setInstallID(installId)
        }
        return installId
    }

    private fun makeWorkRequest(kind: String): TrackierWorkRequest {
        val trackierWorkRequest = TrackierWorkRequest(kind, appToken, this.config.env)
        trackierWorkRequest.device = device
        trackierWorkRequest.gaid = gaid
        trackierWorkRequest.refDetails = getReferrerDetails()
        trackierWorkRequest.installID = installId
        trackierWorkRequest.apkAttributes = this.config.getAPKAttributes()
        trackierWorkRequest.sdtk = this.config.getSDKType()

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

    private fun _trackEvent(event: TrackierEvent) {
        val wrkRequest = makeWorkRequest(TrackierWorkRequest.KIND_EVENT)
        wrkRequest.event = event
        TrackierWorkRequest.enqueue(wrkRequest)
    }

    fun trackEvent(event: TrackierEvent) {
        if (!isEnabled || !configLoaded) {
            return
        }
        if (!isInitialized) {
            Factory.logger.warning("Event Tracking request sent before SDK data was initialized")
        }
        if (!isInstallTracked()) {
            CoroutineScope(Dispatchers.IO).launch {
                for (i in 1..5) {
                    delay(1000 * i.toLong())
                    if (isInstallTracked()) {
                        _trackEvent(event)
                        break
                    }
                }
            }
        } else {
            _trackEvent(event)
        }

    }

    private fun getLastSessionTime(): String {
        return Util.getSharedPrefString(this.config.context, Constants.SHARED_PREF_LAST_SESSION_TIME)
    }

    private fun setLastSessionTime(time: String) {
        val prefs = Util.getSharedPref(this.config.context)
        prefs.edit().putString(Constants.SHARED_PREF_LAST_SESSION_TIME, time)
                .apply()
    }

    suspend fun trackSession() {
        val currentTs = Date().time
        val currentTime = Util.dateFormatter.format(currentTs)
        try {
            val lastSessionTime = getLastSessionTime()
            var lastSessTs: Long = 0
            if (lastSessionTime != "") {
                val lst = Util.dateFormatter.parse(lastSessionTime)?.time
                if (lst?.equals(0) == false) {
                    lastSessTs = lst!!
                }
            }
            val sessionDiff = (currentTs - lastSessTs).toInt()
            if (sessionDiff > this.config.getMinSessionDuration()) {
                val wrkRequest = makeWorkRequest(TrackierWorkRequest.KIND_SESSION_TRACK)
                wrkRequest.sessionTime = lastSessionTime
                APIRepository.doWork(wrkRequest)   
            }
        } catch (e: Exception) {}
        setLastSessionTime(currentTime)
    }
}