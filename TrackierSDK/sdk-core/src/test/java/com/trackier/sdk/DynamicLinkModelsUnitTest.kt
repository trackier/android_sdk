package com.trackier.sdk.unit

import com.trackier.sdk.dynamic_link.*
import org.junit.Assert.*
import org.junit.Test

class DynamicLinkModelsUnitTest {

    @Test
    fun `test DynamicLinkConfig initialization`() {
        val config = DynamicLinkConfig(
            installId = "test_install_id",
            appKey = "test_app_key",
            templateId = "test_template_id",
            link = "https://example.com"
        )
        
        assertEquals("test_install_id", config.installId)
        assertEquals("test_app_key", config.appKey)
        assertEquals("test_template_id", config.templateId)
        assertEquals("https://example.com", config.link)
        assertNull(config.brandDomain)
        assertNull(config.deepLinkValue)
        assertNull(config.sdkParameter)
        assertNull(config.redirection)
        assertNull(config.attrParameter)
        assertNull(config.socialMedia)
    }

    @Test
    fun `test DynamicLinkConfig with all parameters`() {
        val sdkParams = mapOf("param1" to "value1", "param2" to "value2")
        val redirection = Redirection("android://", "ios://", "https://")
        val attrParams = mapOf("attr1" to "value1")
        val socialMedia = SocialMedia("Title", "Description", "image.jpg")
        
        val config = DynamicLinkConfig(
            installId = "test_install_id",
            appKey = "test_app_key",
            templateId = "test_template_id",
            link = "https://example.com",
            brandDomain = "example.com",
            deepLinkValue = "deeplink_value",
            sdkParameter = sdkParams,
            redirection = redirection,
            attrParameter = attrParams,
            socialMedia = socialMedia
        )
        
        assertEquals("test_install_id", config.installId)
        assertEquals("example.com", config.brandDomain)
        assertEquals("deeplink_value", config.deepLinkValue)
        assertEquals(sdkParams, config.sdkParameter)
        assertEquals(redirection, config.redirection)
        assertEquals(attrParams, config.attrParameter)
        assertEquals(socialMedia, config.socialMedia)
    }

    @Test
    fun `test DynamicLinkConfig toMutableMap`() {
        val config = DynamicLinkConfig(
            installId = "test_install_id",
            appKey = "test_app_key",
            templateId = "test_template_id",
            link = "https://example.com"
        )
        
        val map = config.toMutableMap()
        
        assertEquals("test_install_id", map["install_id"])
        assertEquals("test_app_key", map["app_key"])
        assertEquals("test_template_id", map["template_id"])
        assertEquals("https://example.com", map["link"])
        assertEquals("", map["brand_domain"])
        assertEquals("", map["deep_link_value"])
        assertNotNull(map["sdk_parameter"])
        assertNotNull(map["redirection"])
        assertNotNull(map["attr_parameter"])
        assertNotNull(map["social_media"])
    }

    @Test
    fun `test DynamicLinkResponse initialization`() {
        val response = DynamicLinkResponse(
            success = true,
            message = "Success",
            error = null,
            data = null
        )
        
        assertTrue(response.success)
        assertEquals("Success", response.message)
        assertNull(response.error)
        assertNull(response.data)
    }

    @Test
    fun `test DynamicLinkResponse with error`() {
        val error = ErrorResponse(400, "BAD_REQUEST", "Bad Request", "Invalid parameters")
        val response = DynamicLinkResponse(
            success = false,
            message = "Error occurred",
            error = error,
            data = null
        )
        
        assertFalse(response.success)
        assertEquals("Error occurred", response.message)
        assertEquals(error, response.error)
        assertNull(response.data)
    }

    @Test
    fun `test ErrorResponse initialization`() {
        val error = ErrorResponse(500, "INTERNAL_ERROR", "Internal Server Error", "Something went wrong")
        
        assertEquals(500, error.statusCode)
        assertEquals("INTERNAL_ERROR", error.errorCode)
        assertEquals("Internal Server Error", error.codeMsg)
        assertEquals("Something went wrong", error.message)
    }

    @Test
    fun `test LinkData initialization`() {
        val linkData = LinkData("https://example.com/dynamic-link")
        
        assertEquals("https://example.com/dynamic-link", linkData.link)
    }

    @Test
    fun `test Redirection initialization`() {
        val redirection = Redirection("android://", "ios://", "https://")
        
        assertEquals("android://", redirection.android)
        assertEquals("ios://", redirection.ios)
        assertEquals("https://", redirection.desktop)
    }

    @Test
    fun `test Redirection toMutableMap`() {
        val redirection = Redirection("android://", "ios://", "https://")
        val map = redirection.toMutableMap()
        
        assertEquals("android://", map["android"])
        assertEquals("ios://", map["ios"])
        assertEquals("https://", map["desktop"])
    }

    @Test
    fun `test SocialMedia initialization`() {
        val socialMedia = SocialMedia("Test Title", "Test Description", "test-image.jpg")
        
        assertEquals("Test Title", socialMedia.title)
        assertEquals("Test Description", socialMedia.description)
        assertEquals("test-image.jpg", socialMedia.image)
    }

    @Test
    fun `test SocialMedia toMutableMap`() {
        val socialMedia = SocialMedia("Test Title", "Test Description", "test-image.jpg")
        val map = socialMedia.toMutableMap()
        
        assertEquals("Test Title", map["title"])
        assertEquals("Test Description", map["description"])
        assertEquals("test-image.jpg", map["image"])
    }
} 