package com.trackier.sdk

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocalInstallReferrer(val context: Context, val delimeter: String) {
    var clickId = ""
    @RequiresApi(Build.VERSION_CODES.M)
    fun getLocalRefDetails(): RefererDetails {
        try {
            if (context.checkSelfPermission(READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath).walk()
                    .forEach {
                        val pattern = Regex("[a-fA-F\\d]{24}\$")
                        if (pattern.containsMatchIn(it.nameWithoutExtension)) {
                            val value = it.nameWithoutExtension.split(delimeter)
                            clickId = "tr_clickid=" + value.get(value.size - 1)
                        }
                    }
                return RefererDetails(clickId, "", "")
            }
            else {
                return RefererDetails.default()
            }
        } catch (e: Exception) {
             return RefererDetails.default()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private suspend fun getInfo(): RefererDetails {
        return suspendCoroutine {
            val rd = getLocalRefDetails()
            it.resume(rd)
        }
    }

    suspend fun getRefDetails(): RefererDetails {
        for (i in 1..5) {
            return getInfo()
        }
        return RefererDetails.default()
    }
}