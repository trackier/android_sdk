package com.trackier.sdk.unit

import com.trackier.sdk.dynamic_link.*
import org.junit.Assert.*
import org.junit.Test

class DynamicLinkParametersUnitTest {

    @Test
    fun `test AndroidParameters builder pattern`() {
        val androidParams = AndroidParameters.Builder()
            .setRedirectLink("android://example.com")
            .build()
        
        assertEquals("android://example.com", androidParams.redirectLink)
    }

    @Test
    fun `test AndroidParameters with empty redirect link`() {
        val androidParams = AndroidParameters.Builder()
            .setRedirectLink("")
            .build()
        
        assertEquals("", androidParams.redirectLink)
    }

    @Test
    fun `test IosParameters builder pattern`() {
        val iosParams = IosParameters.Builder()
            .setRedirectLink("ios://example.com")
            .build()
        
        assertEquals("ios://example.com", iosParams.redirectLink)
    }

    @Test
    fun `test IosParameters with empty redirect link`() {
        val iosParams = IosParameters.Builder()
            .setRedirectLink("")
            .build()
        
        assertEquals("", iosParams.redirectLink)
    }

    @Test
    fun `test DesktopParameters builder pattern`() {
        val desktopParams = DesktopParameters.Builder()
            .setRedirectLink("https://example.com")
            .build()
        
        assertEquals("https://example.com", desktopParams.redirectLink)
    }

    @Test
    fun `test DesktopParameters with empty redirect link`() {
        val desktopParams = DesktopParameters.Builder()
            .setRedirectLink("")
            .build()
        
        assertEquals("", desktopParams.redirectLink)
    }

    @Test
    fun `test SocialMetaTagParameters builder pattern`() {
        val socialParams = SocialMetaTagParameters.Builder()
            .setTitle("Test Title")
            .setDescription("Test Description")
            .setImageLink("https://example.com/image.jpg")
            .build()
        
        assertEquals("Test Title", socialParams.title)
        assertEquals("Test Description", socialParams.description)
        assertEquals("https://example.com/image.jpg", socialParams.imageLink)
    }

    @Test
    fun `test SocialMetaTagParameters with empty values`() {
        val socialParams = SocialMetaTagParameters.Builder()
            .setTitle("")
            .setDescription("")
            .setImageLink("")
            .build()
        
        assertEquals("", socialParams.title)
        assertEquals("", socialParams.description)
        assertEquals("", socialParams.imageLink)
    }

    @Test
    fun `test builder chaining`() {
        val androidParams = AndroidParameters.Builder()
            .setRedirectLink("android://example.com")
            .build()
        
        val iosParams = IosParameters.Builder()
            .setRedirectLink("ios://example.com")
            .build()
        
        val desktopParams = DesktopParameters.Builder()
            .setRedirectLink("https://example.com")
            .build()
        
        val socialParams = SocialMetaTagParameters.Builder()
            .setTitle("Title")
            .setDescription("Description")
            .setImageLink("image.jpg")
            .build()
        
        assertNotNull(androidParams)
        assertNotNull(iosParams)
        assertNotNull(desktopParams)
        assertNotNull(socialParams)
    }

    @Test
    fun `test parameter classes are private constructors`() {
        // These classes use private constructors with builder pattern
        // This test verifies the builder pattern works correctly
        val androidParams = AndroidParameters.Builder().build()
        val iosParams = IosParameters.Builder().build()
        val desktopParams = DesktopParameters.Builder().build()
        val socialParams = SocialMetaTagParameters.Builder().build()
        
        assertNotNull(androidParams)
        assertNotNull(iosParams)
        assertNotNull(desktopParams)
        assertNotNull(socialParams)
    }
} 