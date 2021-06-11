package com.trackier.sdk

import android.os.Environment
import android.util.Log
import java.io.File

class LocalInstallReferrer {

    companion object{
        fun getLocalRefDetails(delimeter:String) : RefererDetails{
            var clickId = ""
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath).walk().forEach {
                val pattern = Regex("^[a-bA-Z0-9-_]*_[a-f\\d]{24}\$")
                if(pattern.containsMatchIn(it.nameWithoutExtension)) {
                    val value = it.nameWithoutExtension.split(delimeter)
                    Log.d("openChooser", "apk: :" + value.get(value.size - 1))
                    clickId = value.get(value.size - 1)
                }
            }
            return RefererDetails(clickId, "", "")
        }
    }
}