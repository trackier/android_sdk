package com.trackier.sdk

import androidx.annotation.Keep

@Keep
data class AttributionParams(var parterId: String = "",
                             var siteId: String = "",
                             var subSiteID: String = "",
                             var channel: String = "",
                             var ad : String = "",
                             var adId: String = ""){

    fun getData(): MutableMap<String, Any> {
        val body = mutableMapOf<String, Any>()
        body["pid"] = this.parterId
        body["sid"] = this.siteId
        body["ssid"] = this.subSiteID
        body["ch"] =  this.channel
        body["ad"] = this.ad
        body["adid"] = this.adId
        return body
    }
}