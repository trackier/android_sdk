package com.trackier.sdk

import androidx.annotation.Keep
import java.net.URLDecoder

@Keep
data class RefererDetails(
    val url: String,
    val clickTime: String,
    val installTime: String,
) {
    val clickId : String
    val isOrganic: Boolean get() = clickId.isEmpty()

    init {
        val decodedUrl = URLDecoder.decode(url, "UTF-8").toString()
        val params = Util.getQueryParams(decodedUrl);
        clickId = params["tr_clickid"] ?: ""
    }

    companion object {
        const val ORGANIC_REF = "utm_source=organic"
        fun default(): RefererDetails {
            return RefererDetails(ORGANIC_REF, "", "")
        }
    }
}