package com.trackier.sdk

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class DeeplinkResponse(
    val success: Boolean,
    val url: String?,
    val intent: String?
    //val sdkParams: MutableMap<String, Any>
)
{}