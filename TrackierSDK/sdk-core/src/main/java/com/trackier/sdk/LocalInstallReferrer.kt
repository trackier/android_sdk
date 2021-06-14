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

class LocalInstallReferrer(val context: Context) {
    var clickId = ""

    @RequiresApi(Build.VERSION_CODES.M)
    fun getLocalRefDetails(delimeter:String): RefererDetails {
        try {
            if (context.checkSelfPermission(READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath).walk()
                    .forEach {
                        val pattern = Regex("^[a-bA-Z0-9-_]*_[a-f\\d]{24}\$")
                        if (pattern.containsMatchIn(it.nameWithoutExtension)) {
                            val value = it.nameWithoutExtension.split(delimeter)
                            clickId = value.get(value.size - 1)
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
    private suspend fun getInfo(delimeter:String): RefererDetails {
        return suspendCoroutine {
            val rd = getLocalRefDetails(delimeter)
            it.resume(rd)
        }
    }

    suspend fun getRefDetails(delimeter:String): RefererDetails {
        for (i in 1..5) {
            return getInfo(delimeter)
        }
        return RefererDetails.default()
    }
}