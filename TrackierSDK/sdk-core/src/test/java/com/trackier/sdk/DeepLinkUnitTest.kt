package com.trackier.sdk.unit

import com.trackier.sdk.DeepLink
import org.junit.Assert.*
import org.junit.Test

class DeepLinkUnitTest {
    @Test
    fun `test DeepLink creation`() {
        val deepLink = DeepLink("https://example.com?dlv=abc", false)
        assertNotNull(deepLink)
        assertEquals("https://example.com?dlv=abc", deepLink.getUrl())
        assertFalse(deepLink.isDeferred())
    }

    @Test
    fun `test DeepLink with deferred true and sdkParams`() {
        val params = mapOf("foo" to 123)
        val deepLink = DeepLink("https://example.com?dlv=xyz", true, params)
        assertTrue(deepLink.isDeferred())
        assertEquals("xyz", deepLink.getDeepLinkValue())
        assertEquals(123, deepLink.getSdkParamValue("foo"))
    }
} 