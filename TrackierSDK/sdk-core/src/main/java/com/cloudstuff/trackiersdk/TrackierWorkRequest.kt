package com.cloudstuff.trackiersdk

import androidx.work.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class TrackierWorkRequest(val kind: String = KIND_UNKNOWN) {
    var gaid: String? = null
    var isLAT = false
    lateinit var device: DeviceInfo
    var event = TrackierEvent(Constants.UNKNOWN_EVENT)
    var refDetails = RefererDetails.default()
    private val createdAt = Util.dateFormatter.format(Date())

     var UIID = ""    //change by prak24 20 jan 2021
     var appToken = ""  //change by prak24 20 jan 2021

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
        body["installId"] = UIID  // TODO: fix me  //chnage by prak24 20 jan 2021
        body["appKey"] = appToken    //chnage by prak24 20 jan 2021
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

    companion object {
        const val KIND_UNKNOWN = "unknown"
        const val KIND_INSTALL = "install"
        const val KIND_EVENT = "event"

        private fun getConstraints(): Constraints {
            return Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        }

        fun enqueue(wrk: TrackierWorkRequest) {
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