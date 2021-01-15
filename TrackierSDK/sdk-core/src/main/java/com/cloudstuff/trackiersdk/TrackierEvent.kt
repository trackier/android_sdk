package com.cloudstuff.trackiersdk

data class TrackierEvent(val name: String) {
    var orderId: String? = null
    var currency: String? = null
    var param1: String? = null
    var param2: String? = null
    var param3: String? = null
    var param4: String? = null
    var param5: String? = null
    var param6: String? = null
    var param7: String? = null
    var param8: String? = null
    var param9: String? = null
    var param10: String? = null

    var revenue: Double? = null
    var eventValue = mutableMapOf<String, Any>()

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