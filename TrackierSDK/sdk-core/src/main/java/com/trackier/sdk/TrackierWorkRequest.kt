package com.trackier.sdk

import androidx.work.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class TrackierWorkRequest(val kind: String, private val appToken: String, private val mode: String) {
    var gaid: String? = null
    var isLAT = false
    lateinit var device: DeviceInfo
    var event = TrackierEvent(Constants.UNKNOWN_EVENT)
    var refDetails = RefererDetails.default()
    private val createdAt = Util.dateFormatter.format(Date())
    var installID = ""
    var sessionTime = ""
    var sdtk = ""
    var attributionParams: AttributionParams? = null
    var customerId = ""
    var customerEmail = ""
    var customerOptionals: MutableMap<String, Any>? = null
    var disableOrganicTrack = false

    private fun setDefaults(): MutableMap<String, Any> {
        val body = mutableMapOf<String, Any>()
        body["device"] = this.device
        body["createdAt"] = createdAt
        if (gaid?.isBlank() == false) {
            body["gaid"] = gaid!!
        }
        body["isLAT"] = isLAT
        body["clickId"] = refDetails.clickId
        body["clickTime"] = refDetails.clickTime
        body["installTime"] = refDetails.installTime
        body["installId"] = installID
        body["appKey"] = appToken
        body["mode"] = mode
        body["sdkt"] = sdtk
        body["cuid"] = customerId
        body["cmail"] = customerEmail
        body["installTimeMicro"] = Util.getTimeInUnix(refDetails.installTime)
        if (customerOptionals != null) {
            body["opts"] = customerOptionals!!
        }

        val adnAttributes = this.attributionParams?.getData()
        if (adnAttributes != null) {
            for ((k, v) in adnAttributes) {
                body[k] = v
            }
        }
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

    companion object {
        const val KIND_UNKNOWN = "unknown"
        const val KIND_INSTALL = "install"
        const val KIND_EVENT = "event"
        const val KIND_SESSION_TRACK = "session_track"

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
            val adapter: JsonAdapter<TrackierWorkRequest> = moshi.adapter(TrackierWorkRequest::class.java)
            val json = adapter.toJson(wrk)
            val constraints = getConstraints()

            val myWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<BackgroundWorker>()
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS)
                .setConstraints(constraints)
                .addTag(Constants.LOG_WORK_TAG)
                .setInputData(workDataOf(Constants.LOG_WORK_INPUT_KEY to json))
                .build()
            WorkManager.getInstance().enqueue(myWorkRequest)
        }
    }
}
