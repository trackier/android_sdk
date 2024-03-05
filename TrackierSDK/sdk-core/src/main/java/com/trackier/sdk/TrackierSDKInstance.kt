package com.trackier.sdk

import android.content.Context
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class TrackierSDKInstance {
    private val device = DeviceInfo()
    lateinit var config: TrackierSDKConfig
    private var refDetails: RefererDetails? = null
    private var refXiaomiDetails: XiaomiReferrerDetails? = null
    private var appToken: String = ""
    var isEnabled = true
    var isInitialized = false
    var configLoaded = false
    var gaid: String? = null
    var isLAT = false
    var installId = ""
    var isLocalRefEnabled = false
    var localRefDelimeter = ""
    var isManualInstall = false
    var disableOrganicTrack = false

    var customerId = ""
    var customerEmail = ""
    var customerName = ""
    var customerPhoneNumber = ""
    var customerOptionals: MutableMap<String, Any>? = null

    var firstInstallTime = ""
    var organic = false
    var gender = ""
    var dob = ""
    var preinstallData: MutableMap<String, Any>? = null
    

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
        this.firstInstallTime = getFirstInstallTS()
        this.isManualInstall = config.getManualMode()
        this.disableOrganicTrack = config.getOrganicTracking()
        DeviceInfo.init(device, this.config.context)
        CoroutineScope(Dispatchers.IO).launch {
            for (i in 1..5) {
               val gadid =  initGaid()
                if(!"".equals(gadid)){
                    break
                }
                delay(1000 * i.toLong())
            }
            if (!isManualInstall) {
                initAttributionInfo()
                trackInstall()
            }
            trackSession()
            callDeepLinkListener()
        }
    }

    private suspend fun initGaid(): String {
        for (i in 1..5) {
            val (gaid, isLat) = DeviceInfo.getGAID(this.config.context)
            this.gaid = gaid
            this.isLAT = isLat
            if (this.gaid != null) {
                break
            }
            delay(1000 * i.toLong())
        }
        return this.gaid.toString()
    }

    private suspend fun initAttributionInfo() {
        isInitialized = true
    }

    fun fireInstall() {
        CoroutineScope(Dispatchers.IO).launch {
            initAttributionInfo()
            trackInstall()
            trackSession()
        }
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
        try {
            val prefs = Util.getSharedPref(this.config.context)
            prefs.edit().putString(Constants.SHARED_PREF_INSTALL_URL, refererDetails.url)
                    .putString(Constants.SHARED_PREF_CLICK_TIME, refererDetails.clickTime)
                    .putString(Constants.SHARED_PREF_INSTALL_TIME, refererDetails.installTime)
                    .apply()
        } catch (ex: Exception) {}
    }
    
    private fun setXiaomiReferrerDetails(xiaomiRefererDetails: XiaomiReferrerDetails) {
        refXiaomiDetails = xiaomiRefererDetails
        try {
            val prefs = Util.getSharedPref(this.config.context)
            prefs.edit().putString(Constants.SHARED_PREF_XIAOMI_INSTALL_URL, refXiaomiDetails!!.installReferrer)
                .putInt(Constants.SHARED_PREF_XIAOMI_CLICKTIMESTAMP, refXiaomiDetails!!.referrerClickTimestampSeconds)
                .putInt(Constants.SHARED_PREF_XIAOMI_INSTALLTIMEBEGIN, refXiaomiDetails!!.installBeginTimestampSeconds)
                .apply()
        } catch (ex: Exception) {}
    }

    private fun setInstallID(installID: String) {
        Util.setSharedPrefString(this.config.context, Constants.SHARED_PREF_INSTALL_ID, installID)
    }

    private fun getInstallID(): String {
        var installId = Util.getSharedPrefString(this.config.context, Constants.SHARED_PREF_INSTALL_ID)
        if(installId.isBlank()){
            installId = UUID.randomUUID().toString()
            setInstallID(installId)
        }
        return installId
    }

    private fun setFirstInstallTS(firstInstall: String) {
        Util.setSharedPrefString(this.config.context, Constants.SHARED_PREF_FIRST_INSTALL, firstInstall)
    }

    private fun getFirstInstallTS(): String {
        var firstInstallTime = Util.getSharedPrefString(this.config.context, Constants.SHARED_PREF_FIRST_INSTALL)
        if (firstInstallTime.isBlank()) {
            firstInstallTime = Util.dateFormatter.format(Date())
            setFirstInstallTS(firstInstallTime)
        }
        return firstInstallTime
    }


    private fun makeWorkRequest(kind: String): TrackierWorkRequest {
        val trackierWorkRequest = TrackierWorkRequest(kind, appToken, this.config.env)
        if (this.config.getSDKType() != "android") {
            device.sdkVersion = this.config.getSDKVersion()
        } else {
           device.sdkVersion = Constants.SDK_VERSION
        }
        if (this.config.getAndroidId().isNotEmpty()) {
            device.androidId = this.config.getAndroidId()
        }
        trackierWorkRequest.device = device
        trackierWorkRequest.gaid = gaid
        trackierWorkRequest.refDetails = getReferrerDetails()
        trackierWorkRequest.installID = installId
        trackierWorkRequest.customerId = this.customerId
        trackierWorkRequest.customerEmail = this.customerEmail
        trackierWorkRequest.customerOptionals = this.customerOptionals
        trackierWorkRequest.attributionParams = this.config.getAttributionParams()
        trackierWorkRequest.sdkt = this.config.getSDKType()
        trackierWorkRequest.disableOrganicTrack = disableOrganicTrack
        trackierWorkRequest.firstInstallTime = firstInstallTime
        trackierWorkRequest.organic = organic
        trackierWorkRequest.gender = gender
        trackierWorkRequest.dob = dob
        trackierWorkRequest.secretId = this.config.getAppSecretId()
        trackierWorkRequest.secretKey = this.config.getAppSecretKey()
        trackierWorkRequest.customerName = this.customerName
        trackierWorkRequest.customerPhoneNumber = this.customerPhoneNumber
        trackierWorkRequest.preinstallData = this.preinstallData
        trackierWorkRequest.storeRetargeting = getRetargetingData()
        
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
        try {
            val prefs = Util.getSharedPref(this.config.context)
            prefs.edit().putBoolean(Constants.SHARED_PREF_IS_INSTALL_TRACKED, true).apply()
        } catch (ex: Exception) {}
    }

    private suspend fun trackInstall() {
        if (!isEnabled || !configLoaded) {
            return
        }
        if (isInstallTracked()) {
            return
        }
        try {
            if (!isReferrerStored()) {
                if (isLocalRefEnabled) {
                    val installRef = LocalInstallReferrer(this.config.context, this.localRefDelimeter)
                    val refDetails = installRef.getRefDetails()
                    this.setReferrerDetails(refDetails)
                } else {
                    val installRef = InstallReferrer(this.config.context)
                    val refDetails = installRef.getRefDetails()
                    this.setReferrerDetails(refDetails)
                    val xiaomiInstallRef = installRef.getXiaomiRefDetails()
                    if (xiaomiInstallRef != null) {
                        this.setXiaomiReferrerDetails(xiaomiInstallRef)
                    }
                }
                
            }
        } catch (ex: Exception) {
            Factory.logger.warning("Unable to get referrer data on install")
        }
        preinstallData = Util.getPreLoadAndPAIdata(config.context)
        val wrkRequest = makeWorkRequest(TrackierWorkRequest.KIND_INSTALL)
        try {
            TrackierWorkRequest.enqueue(wrkRequest)
        } catch (ex: Exception) {
            APIRepository.processWork(wrkRequest)
        }

        setInstallTracked()
    }

    private suspend fun _trackEvent(event: TrackierEvent) {
        val wrkRequest = makeWorkRequest(TrackierWorkRequest.KIND_EVENT)
        wrkRequest.event = event

        try {
            TrackierWorkRequest.enqueue(wrkRequest)
        } catch (ex: Exception) {
            APIRepository.processWork(wrkRequest)
        }
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
            CoroutineScope(Dispatchers.IO).launch {
                _trackEvent(event)
            }
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
        if (!isEnabled || !configLoaded) {
            return
        }
        if (!isInstallTracked()) {
            return
        }
        val currentTs = Date().time
        val currentTime = Util.dateFormatter.format(currentTs)
        try {
            val lastSessionTime = getLastSessionTime()

            val wrkRequest = makeWorkRequest(TrackierWorkRequest.KIND_SESSION_TRACK)
            wrkRequest.sessionTime = lastSessionTime
            val resp = APIRepository.processWork(wrkRequest)
            if (resp?.success == true) {
                setLastSessionTime(currentTime)
            }
        } catch (e: Exception) {}
    }
    
    suspend fun deeplinkData(url: Uri): ResponseData? {
        var deeplinRes: ResponseData? = null
        val wrkRequest = makeWorkRequest(TrackierWorkRequest.KIND_DEEPLINKS)
        wrkRequest.deeplinkUrl = url
        try {
            deeplinRes = APIRepository.processWork(wrkRequest)
        } catch (ex: Exception) {
            APIRepository.doWork(wrkRequest)
        }
        return deeplinRes
    }

    fun callDeepLinkListener() {
        val dlt = this.config.getDeepLinkListener() ?: return
        val isDeeplinkCalled = Util.getSharedPrefString(this.config.context, Constants.SHARED_PREF_DEEP_LINK_CALLED)
        if (isDeeplinkCalled == "true") return

        val dlstr = Util.getSharedPrefString(this.config.context, Constants.SHARED_PREF_DEEP_LINK)
        val dlResult: DeepLink
        if (dlstr.isBlank()) {
            val ref = getReferrerDetails()
            if (!ref.isDeepLink) {
                return
            }
            dlResult = DeepLink(ref.url, true)
        } else {
            Util.delSharedPrefKey(this.config.context, Constants.SHARED_PREF_DEEP_LINK)
            dlResult = DeepLink(dlstr, false)
        }
        Util.setSharedPrefString(this.config.context, Constants.SHARED_PREF_DEEP_LINK_CALLED, "true")
        dlt.onDeepLinking(dlResult)
    }
    
    fun callDeepLinkListenerDynamic(dlObj: ResponseData) {
        val dlt = this.config.getDeepLinkListener() ?: return
        if (dlObj.data?.sdkParams?.isNotEmpty() == true) {
            val list: MutableList<String> = ArrayList()
            dlObj.data.sdkParams.forEach { entry -> list.add("${entry.key}=${entry.value}") }
            val urlString = list.joinToString("&")
            val urlBuilder = dlObj.data.url?.let { StringBuilder(it) }
            urlBuilder?.append("?")?.append(urlString)
            val dlResult = DeepLink(urlBuilder.toString(), false)
            dlt.onDeepLinking(dlResult)
        } else if (dlObj.data?.url?.isNotEmpty() == true) {
            val dlResult = DeepLink(dlObj.data.url, false)
            dlt.onDeepLinking(dlResult)
        }
    }
    
    fun getRetargetingData(): MutableMap<String, Any> {
        val body = mutableMapOf<String, Any>()
        body["rtgtime"] = Util.getSharedPrefString(this.config.context, Constants.STORE_RETARGETING_TIME)
        body["url"] = Util.getSharedPrefString(this.config.context, Constants.STORE_RETARGETING)
        return body
    }
    
    fun parseDeepLink(uri: Uri?) {
        if (uri == null) return
        var resData: ResponseData? = null
        CoroutineScope(Dispatchers.Main).launch {
            try {
                resData = deeplinkData(uri)
            } catch (e: Exception) { }
            
            if (isInitialized) {
                try {
                    if (resData != null) {
                        resData?.let { callDeepLinkListenerDynamic(it) }
                    }
                } catch (e: Exception) {
                }
            }
        }
    }
}