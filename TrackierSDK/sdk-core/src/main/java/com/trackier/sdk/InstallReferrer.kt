package com.trackier.sdk

import android.content.Context
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.miui.referrer.annotation.GetAppsReferrerResponse.Companion.FEATURE_NOT_SUPPORTED
import com.miui.referrer.annotation.GetAppsReferrerResponse.Companion.OK
import com.miui.referrer.annotation.GetAppsReferrerResponse.Companion.SERVICE_UNAVAILABLE
import com.miui.referrer.api.GetAppsReferrerClient
import com.miui.referrer.api.GetAppsReferrerDetails
import com.miui.referrer.api.GetAppsReferrerStateListener
import com.trackier.sdk.Factory.logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class InstallReferrer(private val context: Context) {
    
    private fun setupFinished(
        referrerClient: InstallReferrerClient,
        responseCode: Int
    ): RefererDetails {
        return when (responseCode) {
            InstallReferrerClient.InstallReferrerResponse.OK -> {   // Connection established.
                val response: ReferrerDetails = referrerClient.installReferrer
                val clickTime = if (response.referrerClickTimestampSeconds == 0.toLong()) "" else
                    Util.dateFormatter.format(Date(response.referrerClickTimestampSeconds))
                val installTime = if (response.installBeginTimestampSeconds == 0.toLong())
                    Util.dateFormatter.format(Date())
                else
                    Util.dateFormatter.format(Date(response.installBeginTimestampSeconds))
                return RefererDetails(response.installReferrer, clickTime, installTime)
            }
            // API not available on the current Play Store app.
            InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                return RefererDetails.default()
            }
            InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE,
            InstallReferrerClient.InstallReferrerResponse.DEVELOPER_ERROR -> {
                throw InstallReferrerException("SERVICE_UNAVAILABLE")
            }
            else -> RefererDetails.default()
        }
    }
    
    private suspend fun getInfo(): RefererDetails {
        val referrerClient = InstallReferrerClient.newBuilder(context).build()
        return suspendCancellableCoroutine {
            referrerClient.startConnection(object : InstallReferrerStateListener {
                override fun onInstallReferrerSetupFinished(responseCode: Int) {
                    try {
                        val rd = setupFinished(referrerClient, responseCode)
                        referrerClient.endConnection()
                        it.resume(rd)
                    } catch (ex: Exception) {
                        it.resumeWithException(ex)
                    }
                }
                
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                override fun onInstallReferrerServiceDisconnected() {
                    referrerClient.endConnection()
                    // https://stackoverflow.com/questions/48227346/kotlin-coroutine-throws-java-lang-illegalstateexception-already-resumed-but-go#comment112497041_60549898
                    if (it.isActive) {
                        it.resumeWithException(InstallReferrerException("SERVICE_DISCONNECTED"))
                    }
                }
            })
        }
    }
    
    suspend fun getRefDetails(): RefererDetails {
        for (i in 1..5) {
            try {
                return getInfo()
            } catch (ex: InstallReferrerException) {
                delay(1000 * i.toLong())
            } catch (ex: Exception) {
                delay(1000 * i.toLong())
            }
        }
        return RefererDetails.default()
    }
    
    fun xiaomiReferrer(
        responseCode: Int,
        referrerClient: GetAppsReferrerClient
    ): XiaomiReferrerDetails? {
            when (responseCode) {
                OK -> try {
                    val response: GetAppsReferrerDetails = referrerClient.installReferrer
                    val referrerUrl: String = response.installReferrer.toString()
                    return XiaomiReferrerDetails(referrerUrl, response.referrerClickTimestampSeconds.toInt(), response.installBeginTimestampSeconds.toInt())
                } catch (e: Exception) {
                    logger.info("XiaomiReferrer getInstallReferrer: " + e.message)
                }
                FEATURE_NOT_SUPPORTED -> logger.info("XiaomiReferrer onGetAppsReferrerSetupFinished: FEATURE_NOT_SUPPORTED")
                SERVICE_UNAVAILABLE -> logger.info("XiaomiReferrer onGetAppsReferrerSetupFinished: SERVICE_UNAVAILABLE")
            }
        return null
    }
    
    private suspend fun getXiaomiInfo(): XiaomiReferrerDetails? {
        val referrerClient = GetAppsReferrerClient.newBuilder(context).build()
        return suspendCancellableCoroutine {
            referrerClient.startConnection(object : GetAppsReferrerStateListener {
                override fun onGetAppsReferrerSetupFinished(state: Int) {
                    try {
                        val rd = xiaomiReferrer(state, referrerClient)
                        referrerClient.endConnection()
                        it.resume(rd)
                    } catch (ex: Exception) {
                        it.resumeWithException(ex)
                    }
                }
                override fun onGetAppsServiceDisconnected() {
                   //TODO("Not yet implemented")
                }
            })
        }
    }
    
    suspend fun getXiaomiRefDetails(): XiaomiReferrerDetails? {
        for (i in 1..5) {
            try {
                return getXiaomiInfo()
            } catch (ex: InstallReferrerException) {
                delay(1000 * i.toLong())
            } catch (ex: Exception) {
                delay(1000 * i.toLong())
            }
        }
        return XiaomiReferrerDetails.default()
    }
}