package com.trackier.sdk

import java.util.logging.Logger
import java.util.logging.Level

object Factory {
    @Volatile
    private var config: TrackierSDKConfig? = null
    val logger: Logger = Logger.getLogger(Constants.LOG_TAG)
    init {
        logger.level = Level.SEVERE
    }

    fun setLogLevel(value: Level) {
        logger.level = value
    }

    fun setConfig(sdkConfig: TrackierSDKConfig) {
        if (config != null) {
            logger.warning("TrackierSDKConfig was already initialised – second initialisation ignored.")
            return
        }
        config = sdkConfig
    }

    fun getConfig(): TrackierSDKConfig {
        return config ?: throw IllegalStateException("TrackierSDKConfig is not initialized.")
    }
}