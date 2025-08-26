package com.trackier.sdk

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

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
                
                // Try Facebook first, then Instagram, then Facebook Lite
                when {
                    context.packageManager.resolveContentProvider(FACEBOOK_PROVIDER, 0) != null -> {
                        providerUri = Uri.parse("content://$FACEBOOK_PROVIDER/$facebookAppId")
                    }
                    context.packageManager.resolveContentProvider(INSTAGRAM_PROVIDER, 0) != null -> {
                        providerUri = Uri.parse("content://$INSTAGRAM_PROVIDER/$facebookAppId")
                    }
                    context.packageManager.resolveContentProvider(FACEBOOK_LITE_PROVIDER, 0) != null -> {
                        providerUri = Uri.parse("content://$FACEBOOK_LITE_PROVIDER/$facebookAppId")
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
                
                Log.d(TAG, "Meta referrer data collected: referrer=$installReferrer, timestamp=$actualTimestamp, isCT=$isCT")
                
                val metaReferrerDetails = MetaReferrerDetails(
                    installReferrer = installReferrer ?: "",
                    actualTimestamp = actualTimestamp,
                    isCT = isCT
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
} 