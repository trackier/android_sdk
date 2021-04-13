package com.trackier.sdk

class DeepLink {
    private var data = mutableMapOf<String, String>()
    private val deferred = false

    fun isDeferred(): Boolean {
        return deferred
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

    fun getSub1(): String {
        return Util.getMapStringVal(data, "sub1")
    }

    fun getSub2(): String {
        return Util.getMapStringVal(data, "sub2")
    }

    fun getSub3(): String {
        return Util.getMapStringVal(data, "sub3")
    }

    fun getSub4(): String {
        return Util.getMapStringVal(data, "sub4")
    }

    fun getSub5(): String {
        return Util.getMapStringVal(data, "sub5")
    }

    fun getStringValue(key: String): String {
        return Util.getMapStringVal(data, key)
    }

}