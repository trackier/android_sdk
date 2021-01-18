package com.cloudstuff.trackiersdk

data class RefererDetails(
    val url: String,
    val clickTime: String,
    val installTime: String
) {
    val isOrganic: Boolean get() = clickId.isEmpty()

    val clickId: String
    get() { // TODO:
        return ""
    }

    companion object {
        const val ORGANIC_REF = "utm_source=organic"
        fun default(): RefererDetails {
            return RefererDetails(ORGANIC_REF, "", "")
        }
    }
}