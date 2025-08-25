package com.trackier.sdk

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class MetaReferrerDetails(
    val installReferrer: String,
    val actualTimestamp: Long,
    val isCT: Int,
    val source: String = "", // "facebook", "instagram", "facebook_lite"
    val campaignData: Map<String, Any>? = null
) {
    companion object {
        fun default(): MetaReferrerDetails {
            return MetaReferrerDetails("", 0L, 0)
        }
    }
}