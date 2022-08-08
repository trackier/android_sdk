package com.trackier.sdk

object Constants {
    const val SDK_VERSION = "1.6.29"
    const val USER_AGENT = "com.cloudstuff.trackiersdk:trackier-android:" + SDK_VERSION
    const val API_VERSION = "v1"
    const val BASE_URL = "https://events.trackier.io/" + API_VERSION + "/"
    const val LOG_TAG = "trackiersdk"
    const val LOG_WORK_TAG = "trackiersdk:work"
    const val LOG_WORK_INPUT_KEY = "trackiersdk:work_request"
    const val SHARED_PREF_NAME = "com.trackiersdk"
    const val SHARED_PREF_INSTALL_URL = "install_referrer"
    const val SHARED_PREF_CLICK_TIME = "click_time"
    const val SHARED_PREF_INSTALL_TIME = "install_time"
    const val SHARED_PREF_IS_INSTALL_TRACKED = "is_install_tracked"
    const val SHARED_PREF_INSTALL_ID = "install_id"
    const val SHARED_PREF_DEEP_LINK = "deep_link"
    const val SHARED_PREF_DEEP_LINK_CALLED = "deep_link_called"

    const val SHARED_PREF_LAST_SESSION_TIME = "last_session_time"


    const val SHARED_PREF_FIRST_INSTALL = "first_install"

    const val ENV_PRODUCTION = "production"
    const val ENV_SANDBOX = "sandbox"
    const val ENV_TESTING = "testing"

    const val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    const val UUID_EMPTY = "00000000-0000-0000-0000-000000000000"
    const val UNKNOWN_EVENT = "unknown"

    const val EPOCH_YEAR = 1970
}