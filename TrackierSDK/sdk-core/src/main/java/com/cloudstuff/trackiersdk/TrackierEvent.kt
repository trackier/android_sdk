package com.cloudstuff.trackiersdk

data class TrackierEvent(val id: String) { //changed from name to id
    @JvmField var orderId: String? = null
    @JvmField var currency: String? = null
    @JvmField var param1: String? = null
    @JvmField var param2: String? = null
    @JvmField var param3: String? = null
    @JvmField var param4: String? = null
    @JvmField var param5: String? = null
    @JvmField var param6: String? = null
    @JvmField var param7: String? = null
    @JvmField var param8: String? = null
    @JvmField var param9: String? = null
    @JvmField var param10: String? = null

    @JvmField var revenue: Double? = null
    @JvmField var eventValue = mutableMapOf<String, Any>()

    companion object {
        const val LEVEL_ACHIEVED = "xxx"
        const val ADD_TO_CART = "xxx"
        const val ADD_TO_WISHLIST = "xxx"
        const val COMPLETE_REGISTRATION = "xxx"
        const val TUTORIAL_COMPLETION = "xxx"
        const val PURCHASE = "xxx"
        const val SUBSCRIBE = "xxx"
        const val START_TRIAL = "xxx"
        const val ACHIEVEMENT_UNLOCKED = "xxx"
        const val CONTENT_VIEW = "xxx"
        const val TRAVEL_BOOKING = "xxx"
        const val SHARE = "xxx"
        const val INVITE = "xxx"
        const val LOGIN = "xxx"
        const val UPDATE = "xxx"
    }
}