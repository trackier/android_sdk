package com.trackier.sdk

import android.content.Context
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import java.util.*
import kotlin.Exception
import kotlinx.coroutines.delay
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine

class InstallReferrer(private val context: Context) {

    private fun setupFinished(referrerClient: InstallReferrerClient, responseCode: Int): RefererDetails {
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
}