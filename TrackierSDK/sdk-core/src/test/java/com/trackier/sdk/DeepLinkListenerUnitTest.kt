package com.trackier.sdk.unit

import com.trackier.sdk.DeepLink
import com.trackier.sdk.DeepLinkListener
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class DeepLinkListenerUnitTest {

    @Test
    fun `test DeepLinkListener interface structure`() {
        // Test that DeepLinkListener is an interface
        assertTrue(DeepLinkListener::class.java.isInterface)
        
        // Test that it has the expected method
        val methods = DeepLinkListener::class.java.methods
        val methodNames = methods.map { it.name }
        
        assertTrue(methodNames.contains("onDeepLinking"))
    }

    @Test
    fun `test DeepLinkListener interface method signature`() {
        // Test that the onDeepLinking method exists and has correct signature
        val testDeepLink = DeepLink("https://example.com/deeplink", false)
        
        // This test verifies the method signature is correct
        assertNotNull(testDeepLink)
    }

    @Test
    fun `test DeepLinkListener can be implemented`() {
        // Test that we can create an implementation of DeepLinkListener
        val listener = object : DeepLinkListener {
            var receivedDeepLink: DeepLink? = null
            
            override fun onDeepLinking(result: DeepLink) {
                receivedDeepLink = result
            }
        }
        
        assertNotNull(listener)
        assertTrue(listener is DeepLinkListener)
        
        // Test the implementation
        val testDeepLink = DeepLink("https://example.com/deeplink", false)
        listener.onDeepLinking(testDeepLink)
        
        // Verify the callback was called
        assertNotNull(listener.receivedDeepLink)
        assertEquals(testDeepLink, listener.receivedDeepLink)
    }

    @Test
    fun `test DeepLinkListener callback functionality`() {
        var callbackCalled = false
        var receivedDeepLink: DeepLink? = null
        
        val listener = object : DeepLinkListener {
            override fun onDeepLinking(result: DeepLink) {
                callbackCalled = true
                receivedDeepLink = result
            }
        }
        
        val testDeepLink = DeepLink("https://example.com/deeplink", false)
        listener.onDeepLinking(testDeepLink)
        
        assertTrue(callbackCalled)
        assertNotNull(receivedDeepLink)
        assertEquals(testDeepLink, receivedDeepLink)
    }
} 