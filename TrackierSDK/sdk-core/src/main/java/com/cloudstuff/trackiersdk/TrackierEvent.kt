package com.cloudstuff.trackiersdk

import java.util.Date

data class TrackierEvent(val name: String) {
    var orderId: String? = null;
    var currency: String? = null;
    var param1: String? = null;
    var param2: String? = null;
    var param3: String? = null;
    var param4: String? = null;
    var param5: String? = null;
    var param6: String? = null;
    var param7: String? = null;
    var param8: String? = null;
    var param9: String? = null;
    var param10: String? = null;

    var revenue: Double? = null;
    val createdAt: String = Util.dateFormatter.format(Date())
}