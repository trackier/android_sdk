package com.trackier.sdk

import java.util.logging.Logger
import java.util.logging.Level

object Factory {
    val logger: Logger = Logger.getLogger(Constants.LOG_TAG)
    init {
        logger.level = Level.SEVERE
    }

    fun setLogLevel(value: Level) {
        logger.level = value
    }
}