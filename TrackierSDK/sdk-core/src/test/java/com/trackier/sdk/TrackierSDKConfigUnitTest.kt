package com.trackier.sdk.unit

import android.content.Context
import com.trackier.sdk.TrackierSDKConfig
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.robolectric.RuntimeEnvironment
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class TrackierSDKConfigUnitTest {
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = RuntimeEnvironment.getApplication()
    }

    @Test
    fun `test TrackierSDKConfig creation`() {
        val config = TrackierSDKConfig(context, "token", "development")
        assertEquals(context, config.context)
        assertEquals("token", config.appToken)
        assertEquals("development", config.env)
    }

    @Test
    fun `test property assignment and retrieval`() {
        val config = TrackierSDKConfig(context, "token", "development")
        config.setAppSecret("id", "key")
        config.setSDKType("custom")
        config.setSDKVersion("2.0.0")
        config.setMinSessionDuration(30)
        config.setManualMode(true)
        config.disableOrganicTracking(true)
        config.setAndroidId("android_id")
        assertEquals("id", config.getAppSecretId())
        assertEquals("key", config.getAppSecretKey())
        assertEquals("custom", config.getSDKType())
        assertEquals("2.0.0", config.getSDKVersion())
        assertEquals(30, config.getMinSessionDuration())
        assertTrue(config.getManualMode())
        assertTrue(config.getOrganicTracking())
        assertEquals("android_id", config.getAndroidId())
    }

    @Test
    fun `test region handling`() {
        val config = TrackierSDKConfig(context, "token", "development")
        config.setRegion(TrackierSDKConfig.Region.IN)
        assertEquals("in", config.getRegion())
        config.setRegion(TrackierSDKConfig.Region.GLOBAL)
        assertEquals("global", config.getRegion())
    }
} 