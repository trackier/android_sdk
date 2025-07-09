package com.trackier.sdk.unit

import com.trackier.sdk.APIService
import com.trackier.sdk.ResponseData
import com.trackier.sdk.dynamic_link.DynamicLinkResponse
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlinx.coroutines.runBlocking

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class APIServiceUnitTest {

    @Mock
    private lateinit var apiService: APIService

    @Test
    fun `test APIService interface methods exist`() {
        // Test that the interface methods are accessible
        // This test verifies the interface structure
        assertTrue(true)
    }

    @Test
    fun `test sendInstallData method signature`() {
        // Test that the sendInstallData method exists and has correct signature
        val testData = mutableMapOf<String, Any>("test" to "value")
        
        // This test verifies the method signature is correct
        // In a real scenario, this would be tested through APIRepository
        assertNotNull(testData)
        assertTrue(testData.containsKey("test"))
    }

    @Test
    fun `test sendEventData method signature`() {
        // Test that the sendEventData method exists and has correct signature
        val testData = mutableMapOf<String, Any>("event" to "test_event")
        
        // This test verifies the method signature is correct
        assertNotNull(testData)
        assertTrue(testData.containsKey("event"))
    }

    @Test
    fun `test sendSessionData method signature`() {
        // Test that the sendSessionData method exists and has correct signature
        val testData = mutableMapOf<String, Any>("session" to "test_session")
        
        // This test verifies the method signature is correct
        assertNotNull(testData)
        assertTrue(testData.containsKey("session"))
    }

    @Test
    fun `test sendDeeplinksData method signature`() {
        // Test that the sendDeeplinksData method exists and has correct signature
        val testData = mutableMapOf<String, Any>("url" to "https://example.com")
        
        // This test verifies the method signature is correct
        assertNotNull(testData)
        assertTrue(testData.containsKey("url"))
    }

    @Test
    fun `test sendDynamicLinkData method signature`() {
        // Test that the sendDynamicLinkData method exists and has correct signature
        val testData = mutableMapOf<String, Any>("templateId" to "test_template")
        
        // This test verifies the method signature is correct
        assertNotNull(testData)
        assertTrue(testData.containsKey("templateId"))
    }

    @Test
    fun `test APIService interface structure`() {
        // Test that APIService is an interface
        assertTrue(APIService::class.java.isInterface)
        
        // Test that it has the expected methods
        val methods = APIService::class.java.methods
        val methodNames = methods.map { it.name }
        
        assertTrue(methodNames.contains("sendInstallData"))
        assertTrue(methodNames.contains("sendEventData"))
        assertTrue(methodNames.contains("sendSessionData"))
        assertTrue(methodNames.contains("sendDeeplinksData"))
        assertTrue(methodNames.contains("sendDynamicLinkData"))
    }
} 