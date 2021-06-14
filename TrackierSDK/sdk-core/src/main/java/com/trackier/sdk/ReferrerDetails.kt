package com.trackier.sdk

import androidx.annotation.Keep
import java.net.URLDecoder

@Keep
data class RefererDetails(
    val url: String,
    val clickTime: String,
    val installTime: String,
) {
    val clickId: String
    private val params: Map<String, String>
    val isOrganic: Boolean get() = clickId.isEmpty()
    val isDeepLink: Boolean get() {
        val dlv = params["dlv"] ?: ""
        return dlv.isNotBlank()
    }

    init {
        val decodedUrl = URLDecoder.decode(url, "UTF-8").toString()
        params = Util.getQueryParams(decodedUrl);
        if(params.containsKey("clickId")) {
            clickId = params["tr_clickid"] ?: ""
        }
        else{
            clickId = url
        }
    }

    companion object {
        const val ORGANIC_REF = "utm_source=organic"
        fun default(): RefererDetails {
            return RefererDetails(ORGANIC_REF, "", "")
        }
    }
}