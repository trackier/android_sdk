package com.trackier.sdk

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import java.util.*
import org.json.JSONObject
import org.json.JSONException

class MetaInstallReferrer(private val context: Context, private val facebookAppId: String) {
    
    companion object {
        private const val TAG = "MetaInstallReferrer"
        private const val FACEBOOK_PROVIDER = "com.facebook.katana.provider.InstallReferrerProvider"
        private const val INSTAGRAM_PROVIDER = "com.instagram.contentprovider.InstallReferrerProvider"
        private const val FACEBOOK_LITE_PROVIDER = "com.facebook.lite.provider.InstallReferrerProvider"
    }
    
    suspend fun getMetaReferrerDetails(): MetaReferrerDetails {
        // Check if Facebook App ID is valid
        if (facebookAppId.isBlank() || facebookAppId == "your_facebook_app_id_here") {
            Log.d(TAG, "Facebook App ID not configured or invalid, skipping Meta referrer collection")
            return MetaReferrerDetails.default()
        }
        
        for (i in 1..3) {
            try {
                val result = queryMetaContentProvider()
                if (result != null) {
                    return result
                }
                delay(1000 * i.toLong())
            } catch (ex: Exception) {
                Log.d(TAG, "Meta referrer query attempt $i failed: ${ex.message}")
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
                var providerUri: Uri? = null
                var source = ""
                
                // Try Facebook first, then Instagram, then Facebook Lite
                when {
                    context.packageManager.resolveContentProvider(FACEBOOK_PROVIDER, 0) != null -> {
                        providerUri = Uri.parse("content://$FACEBOOK_PROVIDER/$facebookAppId")
                        source = "facebook"
                    }
                    context.packageManager.resolveContentProvider(INSTAGRAM_PROVIDER, 0) != null -> {
                        providerUri = Uri.parse("content://$INSTAGRAM_PROVIDER/$facebookAppId")
                        source = "instagram"
                    }
                    context.packageManager.resolveContentProvider(FACEBOOK_LITE_PROVIDER, 0) != null -> {
                        providerUri = Uri.parse("content://$FACEBOOK_LITE_PROVIDER/$facebookAppId")
                        source = "facebook_lite"
                    }
                    else -> {
                        Log.d(TAG, "No Meta content providers found")
                        continuation.resume(null)
                        return@suspendCancellableCoroutine
                    }
                }
                
                cursor = context.contentResolver.query(providerUri, projection, null, null, null)
                if (cursor == null || !cursor.moveToFirst()) {
                    Log.d(TAG, "No data from Meta content provider")
                    continuation.resume(null)
                    return@suspendCancellableCoroutine
                }
                
                val installReferrerIndex = cursor.getColumnIndex("install_referrer")
                val timestampIndex = cursor.getColumnIndex("actual_timestamp")
                val isCTIndex = cursor.getColumnIndex("is_ct")
                
                val installReferrer = cursor.getString(installReferrerIndex)
                val actualTimestamp = cursor.getLong(timestampIndex)
                val isCT = cursor.getInt(isCTIndex)
                
                Log.d(TAG, "Meta referrer data: referrer=$installReferrer, timestamp=$actualTimestamp, isCT=$isCT, source=$source")
                
                // Parse and decrypt the install referrer if available
                val campaignData = parseInstallReferrer(installReferrer)
                
                val metaReferrerDetails = MetaReferrerDetails(
                    installReferrer = installReferrer ?: "",
                    actualTimestamp = actualTimestamp,
                    isCT = isCT,
                    source = source,
                    campaignData = campaignData
                )
                
                continuation.resume(metaReferrerDetails)
                
            } catch (ex: Exception) {
                Log.e(TAG, "Error querying Meta content provider: ${ex.message}")
                continuation.resumeWithException(ex)
            } finally {
                cursor?.close()
            }
        }
    }
    
    private fun parseInstallReferrer(installReferrer: String?): Map<String, Any>? {
        if (installReferrer.isNullOrBlank()) {
            return null
        }
        
        return try {
            val jsonObject = JSONObject(installReferrer)
            
            val campaignData = mutableMapOf<String, Any>()
            
            // Extract basic UTM parameters
            jsonObject.optString("utm_campaign")?.let { if (it.isNotEmpty()) campaignData["utm_campaign"] = it }
            jsonObject.optString("utm_source")?.let { if (it.isNotEmpty()) campaignData["utm_source"] = it }
            jsonObject.optString("utm_content")?.let { if (it.isNotEmpty()) campaignData["utm_content"] = it }
            jsonObject.optString("utm_medium")?.let { if (it.isNotEmpty()) campaignData["utm_medium"] = it }
            jsonObject.optString("utm_term")?.let { if (it.isNotEmpty()) campaignData["utm_term"] = it }
            
            // Extract utm_content.source data if available
            val utmContent = jsonObject.optJSONObject("utm_content")
            if (utmContent != null) {
                val source = utmContent.optJSONObject("source")
                if (source != null) {
                    val data = source.optString("data")
                    val nonce = source.optString("nonce")
                    
                    if (data.isNotEmpty() && nonce.isNotEmpty()) {
                        // Note: Decryption would require the GPIR decryption key
                        // For now, we store the encrypted data
                        campaignData["encrypted_data"] = data
                        campaignData["nonce"] = nonce
                        campaignData["app_id"] = utmContent.optString("a", "")
                        campaignData["timestamp"] = utmContent.optString("t", "")
                    }
                }
            }
            
            campaignData
        } catch (ex: JSONException) {
            Log.e(TAG, "Error parsing install referrer JSON: ${ex.message}")
            null
        } catch (ex: Exception) {
            Log.e(TAG, "Error processing install referrer: ${ex.message}")
            null
        }
    }
} 