package com.trackier.sdk.unit

import com.trackier.sdk.TrackierSDKInstance
import com.trackier.sdk.TrackierSDKConfig
import com.trackier.sdk.RefererDetails
import com.trackier.sdk.Constants
import com.trackier.sdk.Util
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.After
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.RuntimeEnvironment
import android.content.Context
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.Mock
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class TrackierSDKInstanceUnitTest {
    private lateinit var instance: TrackierSDKInstance
    private lateinit var context: Context
    private lateinit var config: TrackierSDKConfig

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        context = RuntimeEnvironment.getApplication()
        config = TrackierSDKConfig(context, "test_token", "sandbox")
        instance = TrackierSDKInstance()
    }

    @After
    fun tearDown() {
        // Clean up if needed
    }

    @Test
    fun testInitialState() {
        assertFalse(instance.isInitialized)
        assertFalse(instance.configLoaded)
        assertTrue(instance.isEnabled)
    }

    @Test
    fun testInitializeSetsConfigAndFlags() {
        instance.initialize(config)
        assertTrue(instance.configLoaded)
        assertEquals(config, instance.config)
        assertEquals("test_token", instance.config.appToken)
        assertEquals("sandbox", instance.config.env)
    }

    @Test
    fun testGetInstallIDGeneratesUUID() {
        instance.config = config
        val id1 = invokePrivateGetInstallID(instance)
        assertNotNull(id1)
        assertTrue(id1.isNotBlank())
        // Should be a valid UUID
        UUID.fromString(id1)
        // Should return the same value if called again
        val id2 = invokePrivateGetInstallID(instance)
        assertEquals(id1, id2)
    }

    @Test
    fun testSetAndGetFirstInstallTS() {
        instance.config = config
        val ts1 = invokePrivateGetFirstInstallTS(instance)
        assertNotNull(ts1)
        assertTrue(ts1.isNotBlank())
        // Should return the same value if called again
        val ts2 = invokePrivateGetFirstInstallTS(instance)
        assertEquals(ts1, ts2)
    }

    @Test
    fun testSetAndGetRefererDetails() {
        instance.config = config
        val details = RefererDetails("test_url", "test_click", "test_install")
        invokePrivateSetRefererDetails(instance, details)
        val retrieved = invokePrivateGetRefererDetails(instance)
        assertEquals(details.url, retrieved.url)
        assertEquals(details.clickTime, retrieved.clickTime)
        assertEquals(details.installTime, retrieved.installTime)
    }

    // --- Reflection helpers for private methods ---
    private fun invokePrivateGetInstallID(instance: TrackierSDKInstance): String {
        val method = TrackierSDKInstance::class.java.getDeclaredMethod("getInstallID")
        method.isAccessible = true
        return method.invoke(instance) as String
    }
    private fun invokePrivateGetFirstInstallTS(instance: TrackierSDKInstance): String {
        val method = TrackierSDKInstance::class.java.getDeclaredMethod("getFirstInstallTS")
        method.isAccessible = true
        return method.invoke(instance) as String
    }
    private fun invokePrivateSetRefererDetails(instance: TrackierSDKInstance, details: RefererDetails) {
        val method = TrackierSDKInstance::class.java.getDeclaredMethod("setReferrerDetails", RefererDetails::class.java)
        method.isAccessible = true
        method.invoke(instance, details)
    }
    private fun invokePrivateGetRefererDetails(instance: TrackierSDKInstance): RefererDetails {
        val method = TrackierSDKInstance::class.java.getDeclaredMethod("getReferrerDetails")
        method.isAccessible = true
        return method.invoke(instance) as RefererDetails
    }
} 