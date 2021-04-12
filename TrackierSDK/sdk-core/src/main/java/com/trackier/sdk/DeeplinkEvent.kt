package com.trackier.sdk

import android.net.Uri

class DeeplinkEvent{

    interface DeeplinkListner {
        fun receivedDeeplink(deeplink: Uri)
    }

    var listener: DeeplinkListner? = null

    init {
        listener = null
    }

    fun setDeeplinkListener(listener: DeeplinkListner){
        this.listener = listener
    }
}