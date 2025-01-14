package com.trackier.sdk

import androidx.annotation.Keep

@Keep
data class XiaomiReferrerDetails (
    var installReferrer: String,
    var referrerClickTimestampSeconds: Int = -1,
    var installBeginTimestampSeconds: Int = -1
) {
    init {
        this.installReferrer = installReferrer
    }
    
    companion object {
        const val ORGANIC_REF = "utm_source=organic"
        fun default(): XiaomiReferrerDetails {
            return XiaomiReferrerDetails(ORGANIC_REF, -1, -1)
        }
    }
}
