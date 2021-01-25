package com.cloudstuff.trackiersdk

import android.net.Uri
import android.util.Log
import java.net.URLDecoder

data class RefererDetails(
        val url: String,
        val clickTime: String,
        val installTime: String,
) {
    val isOrganic: Boolean get() = clickId.isEmpty()

    val clickId : String

        init {
            val afterDecode = URLDecoder.decode(url, "UTF-8").toString()
            val params = getQueryKeyValueMap(afterDecode);
            clickId = if (params != null) params["tr_clickid"].toString() else ""
        }

    fun getQueryKeyValueMap(url: String): Map<String, String> {
        val map = url.split("?").associate {
            val (left, right) = url.split("=")
            left to right
        }
        return map
    }

    companion object {
        const val ORGANIC_REF = "utm_source=organic"
        fun default(): RefererDetails {
            return RefererDetails(ORGANIC_REF, "", "")
        }
    }
}