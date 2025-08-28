package com.trackier.sdk.dynamic_link

interface UnityDynamicLinkCallback {
    fun onSuccess(link: String)
    fun onFailure(error: String)
}