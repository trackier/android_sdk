package com.trackier.sdk

class DeepLinkResult {
    fun getDeepLink(): DeepLink {
        var obj = DeepLink("ls", false)
        return obj
    }

    fun getStatus(): String {
        return ""
    }

    fun getError(): String {
        return ""
    }
}
