package com.cloudstuff.trackiersdk

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
        clickId = params["tr_clickid"] ?: ""
    }

    private fun getQueryKeyValueMap(url: String): Map<String, String> {
        return url.split("?").associate {
            val (left, right) = url.split("=")
            left to right
        }
    }

    companion object {
        const val ORGANIC_REF = "utm_source=organic"
        fun default(): RefererDetails {
            return RefererDetails(ORGANIC_REF, "", "")
        }
    }
}