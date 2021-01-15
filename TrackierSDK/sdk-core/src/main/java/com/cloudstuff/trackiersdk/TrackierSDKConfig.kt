package com.cloudstuff.trackiersdk

import android.content.Context
import java.util.logging.Level

class TrackierSDKConfig(var context: Context, val appToken: String, val env: String) {
    private val logger = Factory.logger

    init {
        context = context.applicationContext
        setLogLevel(if (env == Constants.ENV_PRODUCTION) Level.SEVERE else Level.FINEST)
    }

    fun setLogLevel(value: Level) {
        logger.level = value
    }
}