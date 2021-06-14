package com.trackier.sdk

import android.os.Environment
import android.util.Log
import com.android.installreferrer.api.InstallReferrerStateListener
import kotlinx.coroutines.delay
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocalInstallReferrer {
    var clickId = ""
    fun getLocalRefDetails(delimeter:String): RefererDetails {
        try{
        File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath).walk()
            .forEach {
                val pattern = Regex("^[a-bA-Z0-9-_]*_[a-f\\d]{24}\$")
                if (pattern.containsMatchIn(it.nameWithoutExtension)) {
                    val value = it.nameWithoutExtension.split(delimeter)
                    Log.d("openChooser", "apk: :" + value.get(value.size - 1))
                    clickId = value.get(value.size - 1)
                }
            }
        return RefererDetails(clickId, "", "")
        } catch (e: Exception) {
            return return RefererDetails("", "", "")
        }
    }

    private suspend fun getInfo(delimeter:String): RefererDetails {
        return suspendCoroutine {
            val rd = getLocalRefDetails(delimeter)
            it.resume(rd)
        }
    }
    
    suspend fun getRefDetails(delimeter:String): RefererDetails {
        for (i in 1..5) {
            try {
                return getInfo(delimeter)
            } catch (ex: InstallReferrerException) {
                delay(1000 * i.toLong())
            }
        }
        return RefererDetails.default()
    }
}