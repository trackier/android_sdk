package com.trackier.sdk.unit

import com.trackier.sdk.dynamic_link.DynamicLink
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.After
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.junit.Ignore

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class DynamicLinkUnitTest {
    private lateinit var dynamicLink: DynamicLink

    @Before
    fun setUp() {
        dynamicLink = DynamicLink.Builder()
            .setTemplateId("template123")
            .setDomainUriPrefix("https://example.com")
            .build()
    }

    @After
    fun tearDown() {
        // Clean up if needed
    }

    @Test
    fun testDynamicLinkConstruction() {
        assertNotNull(dynamicLink)
    }

    @Test
    fun testDynamicLinkBuilder() {
        val link = DynamicLink.Builder()
            .setTemplateId("template123")
            .setDomainUriPrefix("https://example.com")
            .build()
        assertNotNull(link)
    }

    @Ignore("Requires TrackierSDK to be initialized; integration test only.")
    @Test
    fun testToDynamicLinkConfig() {
        val config = dynamicLink.toDynamicLinkConfig()
        assertEquals("template123", config.templateId)
        assertEquals("https://example.com", config.brandDomain)
    }

    // Add more tests for parsing and public methods if available
} 