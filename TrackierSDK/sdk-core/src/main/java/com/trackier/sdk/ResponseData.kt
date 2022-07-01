package com.trackier.sdk

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class ResponseData(
    val success: Boolean
) {
}