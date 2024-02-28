package com.trackier.sdk

import androidx.work.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class TrackierWorkRequest(
    val kind: String,
    private val appToken: String,
    private val mode: String
) {
    var gaid: String? = null
    var isLAT = false
    lateinit var device: DeviceInfo
    var event = TrackierEvent(Constants.UNKNOWN_EVENT)
    var refDetails = RefererDetails.default()
    private val createdAt = Util.dateFormatter.format(Date())
    var installID = ""
    var sessionTime = ""
    var sdkt = ""
    var attributionParams: AttributionParams? = null
    var customerId = ""
    var customerEmail = ""
    var customerName = ""
    var customerPhoneNumber = ""
    var customerOptionals: MutableMap<String, Any>? = null
    var disableOrganicTrack = false
    var firstInstallTime = ""
    var secretId: String = ""
    var secretKey: String = ""
    var organic = false
    var gender = ""
    var dob = ""
    var preinstallData: MutableMap<String, Any>? = null
    lateinit var storeRetargeting: Map<String, Any>
    var deeplinkUrl = ""
    
    private fun setDefaults(): MutableMap<String, Any> {
        val body = mutableMapOf<String, Any>()
        body["device"] = this.device
        body["createdAt"] = createdAt
        if (gaid?.isBlank() == false) {
            body["gaid"] = gaid!!
        }
        body["isLAT"] = isLAT
        body["referrer"] = refDetails.url
        if (refDetails.isDeepLink) {
            val deeplink = DeepLink(refDetails.url, true)
            body["dlParams"] = deeplink.getData()
        }
        body["clickId"] = refDetails.clickId
        body["clickTime"] = refDetails.clickTime
        if (Util.getYear(refDetails.installTime) == Constants.EPOCH_YEAR) {
            body["installTime"] = firstInstallTime
            body["installTimeMicro"] = Util.getTimeInUnix(firstInstallTime)
        } else {
            body["installTime"] = refDetails.installTime
            body["installTimeMicro"] = Util.getTimeInUnix(refDetails.installTime)
        }
        body["installId"] = installID
        body["appKey"] = appToken
        body["mode"] = mode
        body["sdkt"] = sdkt
        body["cuid"] = customerId
        body["cmail"] = customerEmail
        if (customerOptionals != null) {
            body["opts"] = customerOptionals!!
        }
        if (secretKey.length > 10) {
            body["secretId"] = secretId
            body["sigv"] = "v1.0.0"
            body["signature"] = Util.createSignature(
                installID + ":" + createdAt + ":" + secretId + ":" + gaid,
                secretKey
            )
        }

        val adnAttributes = this.attributionParams?.getData()
        if (adnAttributes != null) {
            for ((k, v) in adnAttributes) {
                body[k] = v
            }
        }
        body["organic"] = organic
        body["gender"] = gender
        body["dob"] = dob
        body["cphone"] = customerPhoneNumber
        body["cname"] = customerName
        body["getPreLoadAndPAIdata"] = preinstallData.toString()
        body["storeRetargeting"] = storeRetargeting
        return body
    }

    fun getData(): MutableMap<String, Any> {
        return setDefaults()
    }

    fun getEventData(): MutableMap<String, Any> {
        val body = setDefaults()
        body["event"] = this.event
        return body
    }

    fun getSessionData(): MutableMap<String, Any> {
        val body = setDefaults()
        body["lastSessionTime"] = this.sessionTime
        return body
    }
    
    fun getDeeplinksData(): MutableMap<String, Any> {
        val body = mutableMapOf<String, Any>()
        body["url"] = this.deeplinkUrl
        body["os"] = this.device.osName
        body["osv"] = this.device.osVersion
        body["sdkv"] = Constants.SDK_VERSION
        body["apv"] = this.device.appVersion.toString()
        body["appKey"] = appToken
        return body
    }

    companion object {
        const val KIND_UNKNOWN = "unknown"
        const val KIND_INSTALL = "install"
        const val KIND_EVENT = "event"
        const val KIND_SESSION_TRACK = "session_track"
        const val KIND_DEEPLINKS = "deeplinks"

        private fun getConstraints(): Constraints {
            return Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        }

        fun enqueue(wrk: TrackierWorkRequest) {
            if (wrk.disableOrganicTrack && wrk.refDetails.clickId.isBlank()) {
                return
            }
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val adapter: JsonAdapter<TrackierWorkRequest> =
                moshi.adapter(TrackierWorkRequest::class.java)
            val json = adapter.toJson(wrk)
            val constraints = getConstraints()

            val myWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<BackgroundWorker>()
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .setConstraints(constraints)
                .addTag(Constants.LOG_WORK_TAG)
                .setInputData(workDataOf(Constants.LOG_WORK_INPUT_KEY to json))
                .build()
            WorkManager.getInstance().enqueue(myWorkRequest)
        }
    }
}
