package com.trackier.sdk.dynamic_link

import android.net.Uri
import android.util.Log
import com.trackier.sdk.TrackierSDK
import com.trackier.sdk.TrackierSDKInstance
import com.trackier.sdk.TrackierWorkRequest

class DynamicLink private constructor() {
    private var templateId: String = ""
    private var link: Uri? = null
    private var domainUriPrefix: String = ""
    private var deepLinkValue: String = ""
    private var androidParameters: AndroidParameters? = null
    private var iosParameters: IosParameters? = null
    private var desktopParameters: DesktopParameters? = null
    private var sdkParameters: Map<String, String> = emptyMap()
    private var socialMetaTagParameters: SocialMetaTagParameters? = null

    private var channel: String = ""
    private var campaign: String = ""
    private var mediaSource: String = ""
    private var p1: String = ""
    private var p2: String = ""
    private var p3: String = ""
    private var p4: String = ""
    private var p5: String = ""

    class Builder {
        private val dynamicLink = DynamicLink()

        fun setTemplateId(templateId: String) = apply { dynamicLink.templateId = templateId }
        fun setLink(link: Uri) = apply { dynamicLink.link = link }
        fun setDomainUriPrefix(domainUriPrefix: String) = apply { dynamicLink.domainUriPrefix = domainUriPrefix }
        fun setDeepLinkValue(deepLinkValue: String) = apply { dynamicLink.deepLinkValue = deepLinkValue }
        fun setAndroidParameters(androidParameters: AndroidParameters) = apply { dynamicLink.androidParameters = androidParameters }
        fun setIosParameters(iosParameters: IosParameters) = apply { dynamicLink.iosParameters = iosParameters }
        fun setDesktopParameters(desktopParameters: DesktopParameters) = apply { dynamicLink.desktopParameters = desktopParameters }
        fun setSDKParameters(sdkParameters: Map<String, String>) = apply { dynamicLink.sdkParameters = sdkParameters }
        fun setSocialMetaTagParameters(socialMetaTagParameters: SocialMetaTagParameters) = apply { dynamicLink.socialMetaTagParameters = socialMetaTagParameters }

        fun setAttributionParameters(
            channel: String = "",
            campaign: String = "",
            mediaSource: String = "",
            p1: String = "",
            p2: String = "",
            p3: String = "",
            p4: String = "",
            p5: String = ""
        ) = apply {
            dynamicLink.channel = channel
            dynamicLink.campaign = campaign
            dynamicLink.mediaSource = mediaSource
            dynamicLink.p1 = p1
            dynamicLink.p2 = p2
            dynamicLink.p3 = p3
            dynamicLink.p4 = p4
            dynamicLink.p5 = p5
        }

        fun build(): DynamicLink {
            return dynamicLink
        }
    }

    fun toDynamicLinkConfig(): DynamicLinkConfig {
        return DynamicLinkConfig(
           installId = TrackierSDK.getTrackierId(),
            appKey = TrackierSDK.getAppToken(),
            templateId = templateId,
            link = link?.toString() ?: "",
            brandDomain = domainUriPrefix,
            deepLinkValue = deepLinkValue,
            sdkParameter = sdkParameters,
            redirection = Redirection(
                android = androidParameters?.redirectLink ?: "",
                ios = iosParameters?.redirectLink ?: "",
                desktop = desktopParameters?.redirectLink ?: ""
            ),
            attrParameter = mapOf(
                "channel" to channel,
                "campaign" to campaign,
                "media_source" to mediaSource,
                "p1" to p1,
                "p2" to p2,
                "p3" to p3,
                "p4" to p4,
                "p5" to p5
            ).filterValues { it.isNotEmpty() },
            socialMedia = socialMetaTagParameters?.let {
                SocialMedia(it.title, it.description, it.imageLink)
            }
        )
    }
}
