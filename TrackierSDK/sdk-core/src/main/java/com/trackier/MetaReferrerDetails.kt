package com.trackier.sdk

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class MetaReferrerDetails(
    val installReferrer: String,
    val actualTimestamp: Long,
    val isCT: Int
) {
    companion object {
        fun default(): MetaReferrerDetails {
            return MetaReferrerDetails("", 0L, 0)
        }
    }
}