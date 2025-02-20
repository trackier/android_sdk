package com.trackier.sdk.dynamic_link
import androidx.annotation.Keep
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
data class DynamicLinkConfig(
    @Json(name = "install_id") val installId: String,
    @Json(name = "app_key") val appKey: String,
    @Json(name = "template_id") val templateId: String,
    @Json(name = "link") val link: String,
    @Json(name = "brand_domain") val brandDomain: String? = null,
    @Json(name = "deep_link_value") val deepLinkValue: String? = null,
    @Json(name = "sdk_parameter") val sdkParameter: Map<String, String>? = null,
    @Json(name = "redirection") val redirection: Redirection? = null,
    @Json(name = "attr_parameter") val attrParameter: Map<String, String>? = null,
    @Json(name = "social_media") val socialMedia: SocialMedia? = null
) {
    fun toMutableMap(): MutableMap<String, Any> {
        return mutableMapOf(
            "install_id" to installId,
            "app_key" to appKey,
            "template_id" to templateId,
            "link" to link,
            "brand_domain" to (brandDomain ?: ""),
            "deep_link_value" to (deepLinkValue ?: ""),
            "sdk_parameter" to (sdkParameter ?: emptyMap<String, String>()),
            "redirection" to (redirection?.toMutableMap() ?: emptyMap<String, String>()),
            "attr_parameter" to (attrParameter ?: emptyMap<String, String>()),
            "social_media" to (socialMedia?.toMutableMap() ?: emptyMap<String, String>())
        )
    }
}

@Keep
@JsonClass(generateAdapter = true)
data class DynamicLinkResponse(
    val success: Boolean,
    val message: String? = "Unknown error",  // ✅ Default value prevents missing field crash
    val error: ErrorResponse? = null,
    val data: LinkData? = null
)


@Keep
@JsonClass(generateAdapter = true)
data class ErrorResponse(
    @Json(name = "statusCode") val statusCode: Int,
    @Json(name = "errorCode") val errorCode: String,
    @Json(name = "codeMsg") val codeMsg: String,
    @Json(name = "message") val message: String
)

@Keep
@JsonClass(generateAdapter = true)
data class LinkData(
    @Json(name = "link") val link: String
)



@Keep
@JsonClass(generateAdapter = true)
data class Redirection(
    @Json(name = "android") val android: String? = null,
    @Json(name = "ios") val ios: String? = null,
    @Json(name = "desktop") val desktop: String? = null
) {
    fun toMutableMap(): MutableMap<String, String> {
        return mutableMapOf(
            "android" to (android ?: ""),
            "ios" to (ios ?: ""),
            "desktop" to (desktop ?: "")
        )
    }
}

@Keep
@JsonClass(generateAdapter = true)
data class SocialMedia(
    @Json(name = "title") val title: String? = null,
    @Json(name = "description") val description: String? = null,
    @Json(name = "image") val image: String? = null
) {
    fun toMutableMap(): MutableMap<String, String> {
        return mutableMapOf(
            "title" to (title ?: ""),
            "description" to (description ?: ""),
            "image" to (image ?: "")
        )
    }
}