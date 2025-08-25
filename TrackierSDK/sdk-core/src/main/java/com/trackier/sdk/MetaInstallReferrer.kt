package com.trackier.sdk

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import org.json.JSONObject
import org.json.JSONException
import kotlin.coroutines.cancellation.CancellationException

class MetaInstallReferrer(private val context: Context, private val facebookAppId: String) {

    companion object {
        private const val TAG = "MetaInstallReferrer"
        private const val FACEBOOK_PROVIDER = "com.facebook.katana.provider.InstallReferrerProvider"
        private const val INSTAGRAM_PROVIDER = "com.instagram.contentprovider.InstallReferrerProvider"
        private const val FACEBOOK_LITE_PROVIDER = "com.facebook.lite.provider.InstallReferrerProvider"
        private const val MAX_RETRIES = 3
        private const val INITIAL_DELAY_MS = 1000L

        // Column names for safe access
        private val PROJECTION = arrayOf("install_referrer", "is_ct", "actual_timestamp")
    }

    data class ProviderInfo(val uri: Uri, val source: String)

    suspend fun getMetaReferrerDetails(): MetaReferrerDetails {
        // Check if Facebook App ID is valid
        if (!isValidFacebookAppId(facebookAppId)) {
            Log.d(TAG, "Facebook App ID not configured or invalid, skipping Meta referrer collection")
            return MetaReferrerDetails.default()
        }

        for (attempt in 1..MAX_RETRIES) {
            try {
                val result = queryMetaContentProvider()
                if (result != null) {
                    return result
                }
                val delayTime = INITIAL_DELAY_MS * attempt
                delay(delayTime)
            } catch (ex: CancellationException) {
                Log.d(TAG, "Meta referrer query cancelled")
                throw ex // Re-throw cancellation to respect coroutine cancellation
            } catch (ex: Exception) {
                Log.d(TAG, "Meta referrer query attempt $attempt failed: ${ex.message}")
                if (attempt == MAX_RETRIES) {
                    break // Don't delay after last attempt
                }
                val delayTime = INITIAL_DELAY_MS * attempt
                delay(delayTime)
            }
        }
        return MetaReferrerDetails.default()
    }

    private fun isValidFacebookAppId(appId: String): Boolean {
        return appId.isNotBlank() && appId != "your_facebook_app_id_here" && appId.matches(Regex("^\\d+$"))
    }

    private suspend fun queryMetaContentProvider(): MetaReferrerDetails? {
        return suspendCancellableCoroutine { continuation ->
            try {
                val providerInfo = resolveMetaContentProvider() ?: run {
                    Log.d(TAG, "No Meta content providers found")
                    continuation.resume(null)
                    return@suspendCancellableCoroutine
                }

                // Inline the query logic instead of using a separate function
                try {
                    context.contentResolver.query(
                        providerInfo.uri,
                        PROJECTION,
                        null,
                        null,
                        null
                    )?.use { cursor ->
                        if (cursor.moveToFirst()) {
                            val details = parseCursorDataSafe(cursor, providerInfo.source)
                            continuation.resume(details)
                        } else {
                            Log.d(TAG, "No data from Meta content provider: ${providerInfo.source}")
                            continuation.resume(null)
                        }
                    } ?: run {
                        Log.d(TAG, "Cursor is null from provider: ${providerInfo.source}")
                        continuation.resume(null)
                    }
                } catch (securityEx: SecurityException) {
                    Log.e(TAG, "Security exception accessing provider ${providerInfo.source}: ${securityEx.message}")
                    continuation.resume(null)
                } catch (illegalArgEx: IllegalArgumentException) {
                    Log.e(TAG, "Illegal argument exception with provider ${providerInfo.source}: ${illegalArgEx.message}")
                    continuation.resume(null)
                } catch (ex: Exception) {
                    Log.e(TAG, "Error querying provider ${providerInfo.source}: ${ex.message}")
                    continuation.resume(null)
                }

            } catch (ex: Exception) {
                Log.e(TAG, "Unexpected error in queryMetaContentProvider: ${ex.message}")
                continuation.resume(null)
            }
        }
    }

    private fun resolveMetaContentProvider(): ProviderInfo? {
        return when {
            context.packageManager.resolveContentProvider(FACEBOOK_PROVIDER, 0) != null -> {
                ProviderInfo(
                    Uri.parse("content://$FACEBOOK_PROVIDER/$facebookAppId"),
                    "facebook"
                )
            }
            context.packageManager.resolveContentProvider(INSTAGRAM_PROVIDER, 0) != null -> {
                ProviderInfo(
                    Uri.parse("content://$INSTAGRAM_PROVIDER/$facebookAppId"),
                    "instagram"
                )
            }
            context.packageManager.resolveContentProvider(FACEBOOK_LITE_PROVIDER, 0) != null -> {
                ProviderInfo(
                    Uri.parse("content://$FACEBOOK_LITE_PROVIDER/$facebookAppId"),
                    "facebook_lite"
                )
            }
            else -> null
        }
    }

    private fun parseCursorDataSafe(cursor: Cursor, source: String): MetaReferrerDetails {
        val installReferrer = getStringFromCursorSafe(cursor, "install_referrer")
        val actualTimestamp = getLongFromCursorSafe(cursor, "actual_timestamp")
        val isCT = getIntFromCursorSafe(cursor, "is_ct")

        Log.d(TAG, "Meta referrer data: referrer=$installReferrer, timestamp=$actualTimestamp, isCT=$isCT, source=$source")

        val campaignData = parseInstallReferrerSafe(installReferrer)

        return MetaReferrerDetails(
            installReferrer = installReferrer,
            actualTimestamp = actualTimestamp,
            isCT = isCT,
            source = source,
            campaignData = campaignData
        )
    }

    private fun getStringFromCursorSafe(cursor: Cursor, columnName: String): String {
        return try {
            val columnIndex = cursor.getColumnIndex(columnName)
            if (columnIndex >= 0) {
                cursor.getString(columnIndex) ?: ""
            } else {
                ""
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error getting string from column $columnName: ${ex.message}")
            ""
        }
    }

    private fun getLongFromCursorSafe(cursor: Cursor, columnName: String): Long {
        return try {
            val columnIndex = cursor.getColumnIndex(columnName)
            if (columnIndex >= 0) {
                cursor.getLong(columnIndex)
            } else {
                0L
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error getting long from column $columnName: ${ex.message}")
            0L
        }
    }

    private fun getIntFromCursorSafe(cursor: Cursor, columnName: String): Int {
        return try {
            val columnIndex = cursor.getColumnIndex(columnName)
            if (columnIndex >= 0) {
                cursor.getInt(columnIndex)
            } else {
                0
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error getting int from column $columnName: ${ex.message}")
            0
        }
    }

    private fun parseInstallReferrerSafe(installReferrer: String?): Map<String, Any>? {
        if (installReferrer.isNullOrBlank()) {
            return null
        }

        return try {
            val jsonObject = JSONObject(installReferrer)
            val campaignData = mutableMapOf<String, Any>()

            // Extract basic UTM parameters safely
            optStringSafe(jsonObject, "utm_campaign")?.takeIf { it.isNotEmpty() }?.let {
                campaignData["utm_campaign"] = it
            }
            optStringSafe(jsonObject, "utm_source")?.takeIf { it.isNotEmpty() }?.let {
                campaignData["utm_source"] = it
            }
            optStringSafe(jsonObject, "utm_content")?.takeIf { it.isNotEmpty() }?.let {
                campaignData["utm_content"] = it
            }
            optStringSafe(jsonObject, "utm_medium")?.takeIf { it.isNotEmpty() }?.let {
                campaignData["utm_medium"] = it
            }
            optStringSafe(jsonObject, "utm_term")?.takeIf { it.isNotEmpty() }?.let {
                campaignData["utm_term"] = it
            }

            // Extract utm_content.source data if available
            val utmContent = optJsonObjectSafe(jsonObject, "utm_content")
            if (utmContent != null) {
                val source = optJsonObjectSafe(utmContent, "source")
                if (source != null) {
                    val data = optStringSafe(source, "data") ?: ""
                    val nonce = optStringSafe(source, "nonce") ?: ""

                    if (data.isNotEmpty() && nonce.isNotEmpty()) {
                        campaignData["encrypted_data"] = data
                        campaignData["nonce"] = nonce
                        campaignData["app_id"] = optStringSafe(utmContent, "a") ?: ""
                        campaignData["timestamp"] = optStringSafe(utmContent, "t") ?: ""
                    }
                }
            }

            campaignData.ifEmpty { null }
        } catch (ex: JSONException) {
            Log.e(TAG, "Error parsing install referrer JSON: ${ex.message}")
            null
        } catch (ex: Exception) {
            Log.e(TAG, "Unexpected error parsing install referrer: ${ex.message}")
            null
        }
    }

    private fun optStringSafe(jsonObject: JSONObject, key: String): String? {
        return try {
            if (jsonObject.has(key)) {
                jsonObject.optString(key, null)
            } else {
                null
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error getting string for key $key: ${ex.message}")
            null
        }
    }

    private fun optJsonObjectSafe(jsonObject: JSONObject, key: String): JSONObject? {
        return try {
            if (jsonObject.has(key) && !jsonObject.isNull(key)) {
                jsonObject.optJSONObject(key)
            } else {
                null
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error getting JSON object for key $key: ${ex.message}")
            null
        }
    }
}