package com.trackier.sdk

import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class TrackierEvent(val id: String) {
    @JvmField var orderId: String? = null
    @JvmField var productId: String?= null
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
    @JvmField
    @Json(name = "c_code")
    var couponCode: String? = null
    @JvmField var discount: Float? = null
    @JvmField var revenue: Double? = null
    @JvmField var ev = mutableMapOf<String, Any>()

    companion object {
        const val LEVEL_ACHIEVED = "1CFfUn3xEY"
        const val ADD_TO_CART = "Fy4uC1_FlN"
        const val ADD_TO_WISHLIST = "AOisVC76YG"
        const val COMPLETE_REGISTRATION = "mEqP4aD8dU"
        const val TUTORIAL_COMPLETION = "99VEGvXjN7"
        const val PURCHASE = "Q4YsqBKnzZ"
        const val SUBSCRIBE = "B4N_In4cIP"
        const val START_TRIAL = "jYHcuyxWUW"
        const val ACHIEVEMENT_UNLOCKED = "xTPvxWuNqm"
        const val CONTENT_VIEW = "Jwzois1ays"
        const val TRAVEL_BOOKING = "yP1-ipVtHV"
        const val SHARE = "dxZXGG1qqL"
        const val INVITE = "7lnE3OclNT"
        const val LOGIN = "o91gt1Q0PK"
        const val UPDATE = "sEQWVHGThl"
    }
}
