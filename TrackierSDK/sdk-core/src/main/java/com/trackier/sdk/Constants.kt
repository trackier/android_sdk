package com.trackier.sdk


object Constants {
    const val SDK_VERSION = "1.6.62"
    const val USER_AGENT = "com.cloudstuff.trackiersdk:trackier-android:" + SDK_VERSION
    const val API_VERSION = "v1"
    const val BASE_URL = "https://events.trackier.io/" + API_VERSION + "/"
    const val BASE_URL_DL = " https://sdkr.apptracking.io/dl/" //"https://eo6vxppz8n3ly04.m.pipedream.net/"
    const val LOG_TAG = "trackiersdk"
    const val LOG_WORK_TAG = "trackiersdk:work"
    const val LOG_WORK_INPUT_KEY = "trackiersdk:work_request"
    const val SHARED_PREF_NAME = "com.trackiersdk"
    const val SHARED_PREF_INSTALL_URL = "install_referrer"
    const val SHARED_PREF_XIAOMI_INSTALL_URL = "xiaomi_install_referrer"
    const val SHARED_PREF_XIAOMI_CLICKTIMESTAMP = "xiaomi_timestamp"
    const val SHARED_PREF_XIAOMI_INSTALLTIMEBEGIN = "xiaomi_installtimebegin"
    const val SHARED_PREF_CLICK_TIME = "click_time"
    const val SHARED_PREF_INSTALL_TIME = "install_time"
    const val SHARED_PREF_IS_INSTALL_TRACKED = "is_install_tracked"
    const val SHARED_PREF_INSTALL_ID = "install_id"
    const val SHARED_PREF_DEEP_LINK = "deep_link"
    const val SHARED_PREF_DEEP_LINK_CALLED = "deep_link_called"
    const val SHARED_PREF_AD = "ad"
    const val SHARED_PREF_ADID = "adid"
    const val SHARED_PREF_ADSET = "adset"
    const val SHARED_PREF_ADSETID = "adsetid"
    const val SHARED_PREF_CAMPAIGN = "campaign"
    const val SHARED_PREF_CAMPAIGNID = "campaignid"
    const val SHARED_PREF_CHANNEL = "channel"
    const val SHARED_PREF_P1 = "p1"
    const val SHARED_PREF_P2 = "p2"
    const val SHARED_PREF_P3 = "p3"
    const val SHARED_PREF_P4 = "p4"
    const val SHARED_PREF_P5 = "p5"
    const val SHARED_PREF_CLICKID = "clickId"
    const val SHARED_PREF_DLV = "dlv"
    const val SHARED_PREF_PID = "pid"
    const val SHARED_PREF_PARTNER = "partner"
    const val SHARED_PREF_ISRETARGETING = "isRetargeting"
    
    const val PRE_INSTALL_MANIFEST_KEY = "TR_PRE_INSTALL_PATH"
    const val SYSTEM_PROPERTIES_PRE_INSTALL_PATH = "ro.trackier.preinstall.path"
    const val SYSTEM_PROPERTIES_DEVICE_BUILD_ANDROID = "ro.build.date.utc"
    const val SYSTEM_PROPERTIES_DEVICE_BUILD_XIAOMI = "ro.boottime.init"
    //const val SYSTEM_PROPERTIES_PRE_INSTALL_PATH = "ro.product.name"
    const val ANDROID_SYSTEM_PROPERTIES_CLASS = "android.os.SystemProperties"
    const val PRE_INSTALL_ATTRIBUTION_CAMPAIGN = "campaign"
    const val PRE_INSTALL_ATTRIBUTION_PID = "pid"
    const val PRE_INSTALL_ATTRIBUTION_CAMPAIGNID = "campaignId"
    const val PRE_DEFINED_PATH1 = "/data/local/tmp/preinstall.trackier"
    const val PRE_DEFINED_PATH2 = "/data/preinstall.trackier"
    const val SYSTEM_PATH = "/system/app/"
    const val PRE_INSTALL_MANIFEST_NAME = "TR_PRE_INSTALL_NAME"
    const val SHARED_PREF_LAST_SESSION_TIME = "last_session_time"
    const val SHARED_PREF_FIRST_INSTALL = "first_install"
    const val DEVICE_BRAND = "xiaomi"
    const val STORE_RETARGETING = "retargeting"
    const val STORE_RETARGETING_TIME = "rtgtime"

    const val ENV_PRODUCTION = "production"
    const val ENV_SANDBOX = "sandbox"
    const val ENV_TESTING = "testing"

    const val DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    const val UUID_EMPTY = "00000000-0000-0000-0000-000000000000"
    const val UNKNOWN_EVENT = "unknown"

    const val EPOCH_YEAR = 1970
}