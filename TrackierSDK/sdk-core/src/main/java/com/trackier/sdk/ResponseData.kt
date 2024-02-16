package com.trackier.sdk

import androidx.annotation.Keep
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class ResponseData(
    val success: Boolean,
    val id: String?,
    var ad: String?,
    var adId: String?,
    var camp: String?,
    var campId: String?,
    var adSet: String?,
    var adSetId: String?,
    var channel: String?,
    var p1: String?,
    var p2: String?,
    var p3: String?,
    var p4: String?,
    var p5: String?,
    var clickId: String?,
    var dlv: String?,
    var pid: String?,
    var partner: String?,
    val isRetargeting: Boolean?
) {
}