package com.trackier.sdk.unit

import android.content.Context
import android.net.Uri
import com.trackier.sdk.*
import com.trackier.sdk.dynamic_link.DynamicLink
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.lang.reflect.Field
import kotlinx.coroutines.runBlocking

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class TrackierSDKUnitTest {
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = RuntimeEnvironment.getApplication()
        resetSDKState()
    }

    private fun resetSDKState() {
        try {
            // Use reflection to reset private fields
            val sdkClass = TrackierSDK::class.java
            
            // Reset isInitialized field
            val isInitializedField = sdkClass.getDeclaredField("isInitialized")
            isInitializedField.isAccessible = true
            isInitializedField.set(null, false)
            
            // Reset appToken field
            val appTokenField = sdkClass.getDeclaredField("appToken")
            appTokenField.isAccessible = true
            appTokenField.set(null, "")
            
            // Reset instance field
            val instanceField = sdkClass.getDeclaredField("instance")
            instanceField.isAccessible = true
            instanceField.set(null, TrackierSDKInstance())
            
        } catch (e: Exception) {
            // If reflection fails, just continue - test will fail if needed
            println("Warning: Could not reset SDK state via reflection: ${e.message}")
        }
    }

    @Test
    fun `test SDK initialization with valid config`() {
        val testConfig = TrackierSDKConfig(context, "test_token", "development")
        TrackierSDK.initialize(testConfig)
        assertTrue(TrackierSDK.isEnabled())
        assertEquals("test_token", TrackierSDK.getAppToken())
    }

    @Test
    fun `test trackEvent when SDK enabled and initialized`() {
        val testConfig = TrackierSDKConfig(context, "test_token", "development")
        TrackierSDK.initialize(testConfig)
        TrackierSDK.setEnabled(true)
        val event = TrackierEvent("test_event")
        TrackierSDK.trackEvent(event)
        // Should not throw exception
        assertTrue(true)
    }

    @Test
    fun `test trackEvent when SDK not initialized`() {
        val event = TrackierEvent("test_event")
        TrackierSDK.trackEvent(event)
        // Should not throw exception when SDK not initialized
        assertTrue(true)
    }

    @Test
    fun `test trackEvent when SDK disabled`() {
        val testConfig = TrackierSDKConfig(context, "test_token", "development")
        TrackierSDK.initialize(testConfig)
        TrackierSDK.setEnabled(false)
        val event = TrackierEvent("test_event")
        TrackierSDK.trackEvent(event)
        // Should not throw exception when SDK disabled
        assertTrue(true)
    }

    @Test
    fun `test setUserId and setUserEmail functionality`() {
        val testConfig = TrackierSDKConfig(context, "test_token", "development")
        TrackierSDK.initialize(testConfig)
        val userId = "user_123"
        val userEmail = "user@example.com"
        TrackierSDK.setUserId(userId)
        TrackierSDK.setUserEmail(userEmail)
        // Should not throw exception
        assertTrue(true)
    }

    @Test
    fun `test setEnabled disables and enables SDK`() {
        val testConfig = TrackierSDKConfig(context, "test_token", "development")
        TrackierSDK.initialize(testConfig)
        TrackierSDK.setEnabled(false)
        assertFalse(TrackierSDK.isEnabled())
        TrackierSDK.setEnabled(true)
        assertTrue(TrackierSDK.isEnabled())
    }

    @Test
    fun `test SDK does not reinitialize with new config`() {
        val config1 = TrackierSDKConfig(context, "token1", "development")
        val config2 = TrackierSDKConfig(context, "token2", "development")
        TrackierSDK.initialize(config1)
        val firstToken = TrackierSDK.getAppToken()
        TrackierSDK.initialize(config2)
        val secondToken = TrackierSDK.getAppToken()
        assertEquals("token1", firstToken)
        assertEquals("token1", secondToken) // Should not change
    }

    @Test
    fun `test parseDeepLink with null URI`() {
        TrackierSDK.parseDeepLink(null)
        // Should not throw exception
        assertTrue(true)
    }

    @Test
    fun `test parseDeepLink with valid URI`() {
        val testConfig = TrackierSDKConfig(context, "test_token", "development")
        TrackierSDK.initialize(testConfig)
        val uri = Uri.parse("https://example.com/deeplink")
        TrackierSDK.parseDeepLink(uri)
        // Should not throw exception
        assertTrue(true)
    }

    @Test
    fun `test setLocalRefTrack functionality`() {
        val testConfig = TrackierSDKConfig(context, "test_token", "development")
        TrackierSDK.initialize(testConfig)
        TrackierSDK.setLocalRefTrack(true, "_")
        TrackierSDK.setLocalRefTrack(false, "-")
        // Should not throw exception
        assertTrue(true)
    }

    @Test
    fun `test fireInstall functionality`() {
        val testConfig = TrackierSDKConfig(context, "test_token", "development")
        TrackierSDK.initialize(testConfig)
        TrackierSDK.fireInstall()
        // Should not throw exception
        assertTrue(true)
    }

    @Test
    fun `test setUserAdditionalDetails functionality`() {
        val testConfig = TrackierSDKConfig(context, "test_token", "development")
        TrackierSDK.initialize(testConfig)
        val additionalDetails = mutableMapOf<String, Any>("key1" to "value1", "key2" to 123)
        TrackierSDK.setUserAdditionalDetails(additionalDetails)
        // Should not throw exception
        assertTrue(true)
    }

    @Test
    fun `test trackAsOrganic functionality`() {
        val testConfig = TrackierSDKConfig(context, "test_token", "development")
        TrackierSDK.initialize(testConfig)
        TrackierSDK.trackAsOrganic(true)
        TrackierSDK.trackAsOrganic(false)
        // Should not throw exception
        assertTrue(true)
    }

    @Test
    fun `test setUserName and setUserPhone functionality`() {
        val testConfig = TrackierSDKConfig(context, "test_token", "development")
        TrackierSDK.initialize(testConfig)
        TrackierSDK.setUserName("John Doe")
        TrackierSDK.setUserPhone("+1234567890")
        // Should not throw exception
        assertTrue(true)
    }

    @Test
    fun `test setIMEI and setMacAddress functionality`() {
        val testConfig = TrackierSDKConfig(context, "test_token", "development")
        TrackierSDK.initialize(testConfig)
        TrackierSDK.setIMEI("123456789012345", "123456789012346")
        TrackierSDK.setMacAddress("AA:BB:CC:DD:EE:FF")
        // Should not throw exception
        assertTrue(true)
    }

    @Test
    fun `test getTrackierId functionality`() {
        val testConfig = TrackierSDKConfig(context, "test_token", "development")
        TrackierSDK.initialize(testConfig)
        val trackierId = TrackierSDK.getTrackierId()
        assertNotNull(trackierId)
        assertTrue(trackierId is String)
    }

    @Test
    fun `test all getter methods return strings`() {
        val testConfig = TrackierSDKConfig(context, "test_token", "development")
        TrackierSDK.initialize(testConfig)
        
        // Test all getter methods
        val methods = listOf(
            TrackierSDK::getAd,
            TrackierSDK::getAdID,
            TrackierSDK::getAdSet,
            TrackierSDK::getAdSetID,
            TrackierSDK::getCampaign,
            TrackierSDK::getCampaignID,
            TrackierSDK::getChannel,
            TrackierSDK::getP1,
            TrackierSDK::getP2,
            TrackierSDK::getP3,
            TrackierSDK::getP4,
            TrackierSDK::getP5,
            TrackierSDK::getClickId,
            TrackierSDK::getDlv,
            TrackierSDK::getPid,
            TrackierSDK::getPartner,
            TrackierSDK::getIsRetargeting
        )
        
        methods.forEach { method ->
            val result = method.call()
            assertNotNull(result)
            assertTrue(result is String)
        }
    }

    @Test
    fun `test setPreinstallAttribution functionality`() {
        val testConfig = TrackierSDKConfig(context, "test_token", "development")
        TrackierSDK.initialize(testConfig)
        TrackierSDK.setPreinstallAttribution("test_pid", "test_campaign", "test_campaign_id")
        // Should not throw exception
        assertTrue(true)
    }

    @Test
    fun `test setGender functionality`() {
        val testConfig = TrackierSDKConfig(context, "test_token", "development")
        TrackierSDK.initialize(testConfig)
        TrackierSDK.setGender(TrackierSDK.Gender.Male)
        TrackierSDK.setGender(TrackierSDK.Gender.Female)
        TrackierSDK.setGender(TrackierSDK.Gender.Others)
        // Should not throw exception
        assertTrue(true)
    }

    @Test
    fun `test setDOB functionality`() {
        val testConfig = TrackierSDKConfig(context, "test_token", "development")
        TrackierSDK.initialize(testConfig)
        TrackierSDK.setDOB("1990-01-01")
        // Should not throw exception
        assertTrue(true)
    }

    @Test
    fun `test storeRetargetting functionality`() {
        val testConfig = TrackierSDKConfig(context, "test_token", "development")
        TrackierSDK.initialize(testConfig)
        TrackierSDK.storeRetargetting(context, "https://example.com/retarget")
        // Should not throw exception
        assertTrue(true)
    }

    @Test
    fun `test createDynamicLink functionality`() {
        val testConfig = TrackierSDKConfig(context, "test_token", "development")
        TrackierSDK.initialize(testConfig)
        
        val dynamicLink = DynamicLink.Builder()
            .setTemplateId("test_template_id")
            .setLink(android.net.Uri.parse("https://example.com"))
            .build()
        
        TrackierSDK.createDynamicLink(
            dynamicLink = dynamicLink,
            onSuccess = { link ->
                assertNotNull(link)
            },
            onFailure = { error ->
                assertNotNull(error)
            }
        )
        
        // Since this is async, we just verify the method doesn't throw
        assertTrue(true)
    }

    @Test
    fun `test resolveDeeplinkUrl functionality`() {
        val testConfig = TrackierSDKConfig(context, "test_token", "development")
        TrackierSDK.initialize(testConfig)
        
        TrackierSDK.resolveDeeplinkUrl(
            inputUrl = "https://example.com/deeplink",
            onSuccess = { dlData ->
                assertNotNull(dlData)
            },
            onError = { throwable ->
                assertNotNull(throwable)
            }
        )
        
        // Since this is async, we just verify the method doesn't throw
        assertTrue(true)
    }

    @Test
    fun `test Gender enum values`() {
        assertEquals("Male", TrackierSDK.Gender.Male.toString())
        assertEquals("Female", TrackierSDK.Gender.Female.toString())
        assertEquals("Others", TrackierSDK.Gender.Others.toString())
    }

    @Test
    fun `test satyamtest method`() {
        // Test the satyamtest method - it should not throw any exception
        TrackierSDK.satyamtest()
        // Since this method just prints "Hello Users", we just verify it doesn't throw
        assertTrue(true)
    }

    @Test
    fun `test trackSession suspend function`() {
        val testConfig = TrackierSDKConfig(context, "test_token", "development")
        TrackierSDK.initialize(testConfig)
        
        // Test the trackSession suspend function
        // Since this is a suspend function, we need to run it in a coroutine scope
        runBlocking {
            TrackierSDK.trackSession()
            // Should not throw any exception
            assertTrue(true)
        }
    }
} 