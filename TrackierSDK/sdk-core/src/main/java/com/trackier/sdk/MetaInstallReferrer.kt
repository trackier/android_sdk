package com.trackier.sdk

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MetaInstallReferrer(private val context: Context, private val facebookAppId: String) {

    companion object {
        private const val TAG = "MetaInstallReferrer"
        private const val FACEBOOK_PROVIDER = "com.facebook.katana.provider.InstallReferrerProvider"
        private const val INSTAGRAM_PROVIDER = "com.instagram.contentprovider.InstallReferrerProvider"
        private const val FACEBOOK_LITE_PROVIDER = "com.facebook.lite.provider.InstallReferrerProvider"
        private const val QUERY_TIMEOUT_MS = 5000L // 5 second timeout
    }

    suspend fun getMetaReferrerDetails(): MetaReferrerDetails {
        // Check if Facebook App ID is valid
        if (facebookAppId.isBlank() || facebookAppId == "your_facebook_app_id_here") {
            Log.d(TAG, "Facebook App ID not configured or invalid, skipping Meta referrer collection")
            return MetaReferrerDetails.default()
        }

        for (i in 1..3) {
            try {
                val result = withTimeoutOrNull(QUERY_TIMEOUT_MS) {
                    queryMetaContentProvider()
                }

                if (result != null) {
                    return result
                } else {
                    Log.w(TAG, "Meta referrer query timed out or returned null on attempt $i")
                }
                delay(1000 * i.toLong())
            } catch (ex: Exception) {
                Log.w(TAG, "Meta referrer query attempt $i failed: ${ex.message}")
                delay(1000 * i.toLong())
            }
        }
        return MetaReferrerDetails.default()
    }

    private suspend fun queryMetaContentProvider(): MetaReferrerDetails? {
        return suspendCancellableCoroutine { continuation ->
            var cursor: Cursor? = null
            try {
                val projection = arrayOf("install_referrer", "is_ct", "actual_timestamp")

                val providerUri = when {
                    context.packageManager.resolveContentProvider(FACEBOOK_PROVIDER, 0) != null -> {
                        Uri.parse("content://$FACEBOOK_PROVIDER/$facebookAppId")
                    }
                    context.packageManager.resolveContentProvider(INSTAGRAM_PROVIDER, 0) != null -> {
                        Uri.parse("content://$INSTAGRAM_PROVIDER/$facebookAppId")
                    }
                    context.packageManager.resolveContentProvider(FACEBOOK_LITE_PROVIDER, 0) != null -> {
                        Uri.parse("content://$FACEBOOK_LITE_PROVIDER/$facebookAppId")
                    }
                    else -> {
                        Log.d(TAG, "No Meta content providers found")
                        continuation.resume(null)
                        return@suspendCancellableCoroutine
                    }
                }

                cursor = context.contentResolver.query(providerUri, projection, null, null, null)
                if (cursor == null) {
                    Log.d(TAG, "Cursor is null from Meta content provider")
                    continuation.resume(null)
                    return@suspendCancellableCoroutine
                }

                if (!cursor.moveToFirst()) {
                    Log.d(TAG, "No data from Meta content provider")
                    continuation.resume(null)
                    return@suspendCancellableCoroutine
                }

                // Safe column index handling
                val installReferrerIndex = cursor.getColumnIndex("install_referrer").takeIf { it >= 0 }
                val timestampIndex = cursor.getColumnIndex("actual_timestamp").takeIf { it >= 0 }
                val isCTIndex = cursor.getColumnIndex("is_ct").takeIf { it >= 0 }

                val installReferrer = installReferrerIndex?.let { cursor.getString(it) } ?: ""
                val actualTimestamp = timestampIndex?.let { cursor.getLong(it) } ?: 0L
                val isCT = isCTIndex?.let { cursor.getInt(it) } ?: 0

                Log.d(TAG, "Meta referrer data collected: referrer=$installReferrer, timestamp=$actualTimestamp, isCT=$isCT")

                val metaReferrerDetails = MetaReferrerDetails(
                    installReferrer = installReferrer,
                    actualTimestamp = actualTimestamp,
                    isCT = isCT
                )

                continuation.resume(metaReferrerDetails)

            } catch (ex: SecurityException) {
                Log.w(TAG, "Permission denied for Meta content provider: ${ex.message}")
                continuation.resume(null)
            } catch (ex: IllegalArgumentException) {
                Log.w(TAG, "Invalid arguments for Meta content provider query: ${ex.message}")
                continuation.resume(null)
            } catch (ex: Exception) {
                Log.e(TAG, "Error querying Meta content provider: ${ex.message}")
                continuation.resumeWithException(ex)
            } finally {
                try {
                    cursor?.close()
                } catch (e: Exception) {
                    Log.w(TAG, "Error closing cursor: ${e.message}")
                }
            }
        }
    }
}