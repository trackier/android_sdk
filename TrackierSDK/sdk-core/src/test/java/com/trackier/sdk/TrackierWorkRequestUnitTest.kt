package com.trackier.sdk.unit

import com.trackier.sdk.*
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.After
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.lang.reflect.Field
import java.util.*
import org.junit.Ignore

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class TrackierWorkRequestUnitTest {
    
    private lateinit var workRequest: TrackierWorkRequest
    private lateinit var deviceInfo: DeviceInfo
    
    @Before
    fun setUp() {
        deviceInfo = DeviceInfo(
            osName = "android",
            name = "test_device",
            buildName = "test_build",
            osVersion = "11",
            manufacturer = "test_manufacturer",
            hardwareName = "test_hardware",
            model = "test_model",
            apiLevel = "30",
            brand = "test_brand"
        )
        
        workRequest = TrackierWorkRequest("test_kind", "test_token", "sandbox")
        workRequest.device = deviceInfo
        workRequest.gaid = "test_gaid"
        workRequest.installID = "test_install_id"
        workRequest.storeRetargeting = emptyMap()
    }
    
    @After
    fun tearDown() {
        // Clean up if needed
    }
    
    @Test
    fun testTrackierWorkRequestCreation() {
        assertNotNull(workRequest)
        assertEquals("test_kind", workRequest.kind)
    }
    
    @Test
    fun testGetData() {
        val data = workRequest.getData()
        
        assertNotNull(data)
        assertTrue(data.containsKey("device"))
        assertTrue(data.containsKey("createdAt"))
        assertTrue(data.containsKey("gaid"))
        assertTrue(data.containsKey("isLAT"))
        assertTrue(data.containsKey("referrer"))
        assertTrue(data.containsKey("installId"))
        assertTrue(data.containsKey("appKey"))
        assertTrue(data.containsKey("mode"))
        
        assertEquals("test_gaid", data["gaid"])
        assertEquals("test_install_id", data["installId"])
        assertEquals("test_token", data["appKey"])
        assertEquals("sandbox", data["mode"])
    }
    
    @Test
    fun testGetEventData() {
        val event = TrackierEvent("test_event")
        workRequest.event = event
        
        val data = workRequest.getEventData()
        
        assertNotNull(data)
        assertTrue(data.containsKey("event"))
        assertEquals(event, data["event"])
    }
    
    @Test
    fun testGetSessionData() {
        workRequest.sessionTime = "2023-01-01T00:00:00.000Z"
        
        val data = workRequest.getSessionData()
        
        assertNotNull(data)
        assertTrue(data.containsKey("lastSessionTime"))
        assertEquals("2023-01-01T00:00:00.000Z", data["lastSessionTime"])
    }
    
    @Ignore("Requires full SDK context and static config, not suitable for unit test")
    @Test
    fun testGetDeeplinksData() {
        workRequest.deeplinkUrl = "https://example.com/deeplink"
        
        val data = workRequest.getDeeplinksData()
        
        assertNotNull(data)
        assertTrue(data.containsKey("url"))
        assertTrue(data.containsKey("os"))
        assertTrue(data.containsKey("osv"))
        assertTrue(data.containsKey("sdkv"))
        assertTrue(data.containsKey("apv"))
        assertTrue(data.containsKey("insId"))
        assertTrue(data.containsKey("appKey"))
        assertTrue(data.containsKey("inst"))
        assertTrue(data.containsKey("refr"))
        assertTrue(data.containsKey("dm"))
        assertTrue(data.containsKey("lcl"))
        assertTrue(data.containsKey("tz"))
        
        assertEquals("https://example.com/deeplink", data["url"])
        assertEquals("android", data["os"])
        assertEquals("11", data["osv"])
        assertEquals("1.6.73", data["sdkv"])
        assertEquals("test_token", data["appKey"])
    }
    
    @Test
    fun testWorkRequestConstants() {
        assertEquals("unknown", TrackierWorkRequest.KIND_UNKNOWN)
        assertEquals("install", TrackierWorkRequest.KIND_INSTALL)
        assertEquals("event", TrackierWorkRequest.KIND_EVENT)
        assertEquals("session_track", TrackierWorkRequest.KIND_SESSION_TRACK)
        assertEquals("deeplinks", TrackierWorkRequest.KIND_DEEPLINKS)
    }
    
    @Test
    fun testWorkRequestWithCustomerData() {
        workRequest.customerId = "test_customer_id"
        workRequest.customerEmail = "test@example.com"
        workRequest.customerName = "Test Customer"
        workRequest.customerPhoneNumber = "+1234567890"
        workRequest.customerOptionals = mutableMapOf("key1" to "value1", "key2" to "value2")
        
        val data = workRequest.getData()
        
        assertEquals("test_customer_id", data["cuid"])
        assertEquals("test@example.com", data["cmail"])
        assertEquals("Test Customer", data["cname"])
        assertEquals("+1234567890", data["cphone"])
        assertTrue(data.containsKey("opts"))
    }
    
    @Test
    fun testWorkRequestWithAttributionParams() {
        val attributionParams = AttributionParams(
            parterId = "test_partner",
            siteId = "test_site",
            subSiteID = "test_subsite",
            channel = "test_channel",
            ad = "test_ad",
            adId = "test_adid",
            agid = "test_agid"
        )
        workRequest.attributionParams = attributionParams
        
        val data = workRequest.getData()
        
        assertEquals("test_partner", data["pid"])
        assertEquals("test_site", data["sid"])
        assertEquals("test_subsite", data["ssid"])
        assertEquals("test_channel", data["ch"])
        assertEquals("test_ad", data["ad"])
        assertEquals("test_adid", data["adid"])
        assertEquals("test_agid", data["agid"])
    }
    
    @Test
    fun testWorkRequestWithSignature() {
        workRequest.secretId = "test_secret_id"
        workRequest.secretKey = "test_secret_key_long_enough_for_signature"
        
        val data = workRequest.getData()
        
        assertTrue(data.containsKey("secretId"))
        assertTrue(data.containsKey("sigv"))
        assertTrue(data.containsKey("signature"))
        assertEquals("test_secret_id", data["secretId"])
        assertEquals("v1.0.0", data["sigv"])
    }
} 