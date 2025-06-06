package com.trackier.sdk

class DeepLink(private val uri: String, val deferred: Boolean, private val sdkParams: Map<String, Any>? = null) {
    private var data: Map<String, String> = Util.getQueryParams(uri)

    fun isDeferred(): Boolean {
        return deferred
    }

    fun getUrl(): String {
        return uri
    }

    fun getData(): Map<String, String> {
        return data
    }

    fun getDeepLinkValue(): String {
        return Util.getMapStringVal(data, "dlv")
    }

    fun getPartnerId(): String {
        return Util.getMapStringVal(data, "pid")
    }

    fun getSiteId(): String {
        return Util.getMapStringVal(data, "sid")
    }

    fun getSubSiteId(): String {
        return Util.getMapStringVal(data, "ssid")
    }

    fun getCampaign(): String {
        return Util.getMapStringVal(data, "camp")
    }

    fun getP1(): String {
        return Util.getMapStringVal(data, "p1")
    }

    fun getP2(): String {
        return Util.getMapStringVal(data, "p2")
    }

    fun getP3(): String {
        return Util.getMapStringVal(data, "p3")
    }

    fun getP4(): String {
        return Util.getMapStringVal(data, "p4")
    }

    fun getP5(): String {
        return Util.getMapStringVal(data, "p5")
    }

    fun getStringValue(key: String): String {
        return Util.getMapStringVal(data, key)
    }

    fun getSdkParams(): Map<String, Any>? {
        return sdkParams
    }

    fun getSdkParamValue(key: String): Any? {
        return sdkParams?.get(key)
    }
}
