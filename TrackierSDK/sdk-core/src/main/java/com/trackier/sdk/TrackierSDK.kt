package com.trackier.sdk

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.annotation.Keep
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.Date

@Keep
object TrackierSDK {
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

    @JvmStatic
    fun isEnabled(): Boolean {
        return instance.isEnabled
    }

    @JvmStatic
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
    fun parseDeepLink(uri: Uri?) {
        uri ?: return
        try {
            instance.parseDeepLink(uri)
        } catch (e: Exception) {
            Log.d("trackiersdk","parseDeeplink "+ e.message)
        }
        
    }

    @JvmStatic
    fun setLocalRefTrack(value: Boolean, delimeter: String = "_") {
        if(value) {
            instance.isLocalRefEnabled = value
            instance.localRefDelimeter = delimeter
        }
    }

    @JvmStatic
     fun fireInstall() {
        instance.fireInstall()
    }

    @JvmStatic
    fun setUserId(userId: String) {
       instance.customerId = userId
    }

    @JvmStatic
    fun setUserEmail(userEmail: String) {
        instance.customerEmail = userEmail
    }

    @JvmStatic
    fun setUserAdditionalDetails(userAdditionalDetails: MutableMap<String, Any>) {
        instance.customerOptionals = userAdditionalDetails
    }

    @JvmStatic
    fun getTrackierId(): String {
        return Util.getSharedPrefString(instance.config.context, Constants.SHARED_PREF_INSTALL_ID)
    }

    @JvmStatic
    fun trackAsOrganic(organic: Boolean) {
        instance.organic = organic
    }

    @JvmStatic
    fun setUserName(userName: String) {
        instance.customerName = userName
    }

    @JvmStatic
    fun setUserPhone(userPhone: String) {
        instance.customerPhoneNumber = userPhone
    }
    
    @JvmStatic
    fun getAd(): String {
        return Util.getSharedPrefString(instance.config.context, Constants.SHARED_PREF_AD)
    }
    
    @JvmStatic
    fun getAdID(): String {
        return Util.getSharedPrefString(instance.config.context, Constants.SHARED_PREF_ADID)
    }
    
    @JvmStatic
    fun getAdSet(): String {
        return Util.getSharedPrefString(instance.config.context, Constants.SHARED_PREF_ADSET)
    }
    
    @JvmStatic
    fun getAdSetID(): String {
        return Util.getSharedPrefString(instance.config.context, Constants.SHARED_PREF_ADSETID)
    }
    
    @JvmStatic
    fun getCampaign(): String {
        return Util.getSharedPrefString(instance.config.context, Constants.SHARED_PREF_CAMPAIGN)
    }
    
    @JvmStatic
    fun getCampaignID(): String {
        return Util.getSharedPrefString(instance.config.context, Constants.SHARED_PREF_CAMPAIGNID)
    }
    
    @JvmStatic
    fun getChannel(): String {
        return Util.getSharedPrefString(instance.config.context, Constants.SHARED_PREF_CHANNEL)
    }
    
    @JvmStatic
    fun getP1(): String {
        return Util.getSharedPrefString(instance.config.context, Constants.SHARED_PREF_P1)
    }
    
    @JvmStatic
    fun getP2(): String {
        return Util.getSharedPrefString(instance.config.context, Constants.SHARED_PREF_P2)
    }
    
    @JvmStatic
    fun getP3(): String {
        return Util.getSharedPrefString(instance.config.context, Constants.SHARED_PREF_P3)
    }
    
    @JvmStatic
    fun getP4(): String {
        return Util.getSharedPrefString(instance.config.context, Constants.SHARED_PREF_P4)
    }
    
    @JvmStatic
    fun getP5(): String {
        return Util.getSharedPrefString(instance.config.context, Constants.SHARED_PREF_P5)
    }
    
    @JvmStatic
    fun getClickId(): String {
        return Util.getSharedPrefString(instance.config.context, Constants.SHARED_PREF_CLICKID)
    }
    
    @JvmStatic
    fun getDlv(): String {
        return Util.getSharedPrefString(instance.config.context, Constants.SHARED_PREF_DLV)
    }
    
    @JvmStatic
    fun getPid(): String {
        return Util.getSharedPrefString(instance.config.context, Constants.SHARED_PREF_PID)
    }
    
    @JvmStatic
    fun getPartner(): String {
        return Util.getSharedPrefString(instance.config.context, Constants.SHARED_PREF_PARTNER)
    }
    
    @JvmStatic
    fun getIsRetargeting(): String {
        return Util.getSharedPrefString(instance.config.context, Constants.SHARED_PREF_ISRETARGETING)
    }
    
    @JvmStatic
    fun setPreinstallAttribution(pid: String, campaign: String, campaignId: String) {
        val prefs = Util.getSharedPref(instance.config.context)
        prefs.edit().putString(Constants.PRE_INSTALL_ATTRIBUTION_PID, pid)
            .putString(Constants.PRE_INSTALL_ATTRIBUTION_CAMPAIGN, campaign)
            .putString(Constants.PRE_INSTALL_ATTRIBUTION_CAMPAIGNID, campaignId)
            .apply()
    }
    
    enum class Gender{
        Male,
        Female,
        Others
    }
    
    @JvmStatic
    fun setGender(gender: Gender) {
        instance.gender = gender.toString()
    }
    
    @JvmStatic
    fun setDOB(dob: String) {
        instance.dob = dob
    }
    
    @JvmStatic
    fun storeRetargetting(context: Context, uri: String) {
        val ctx = context.applicationContext
        Util.setSharedPrefString(ctx, Constants.STORE_RETARGETING, uri)
        Util.setSharedPrefString(ctx, Constants.STORE_RETARGETING_TIME, Util.dateFormatter.format(
            Date()))
    }
}
