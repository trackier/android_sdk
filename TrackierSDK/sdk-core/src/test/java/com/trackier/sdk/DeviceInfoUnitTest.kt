package com.trackier.sdk.unit

import com.trackier.sdk.DeviceInfo
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.After
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.lang.reflect.Field

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class DeviceInfoUnitTest {
    
    @Before
    fun setUp() {
        // Reset any static state if needed
    }
    
    @After
    fun tearDown() {
        // Clean up if needed
    }
    
    @Test
    fun testDeviceInfoCreation() {
        // Create DeviceInfo with explicit values to avoid Build.DEVICE dependency
        val deviceInfo = DeviceInfo(
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
        assertNotNull(deviceInfo)
        assertEquals("android", deviceInfo.osName)
        assertEquals("test_device", deviceInfo.name)
        assertEquals("test_build", deviceInfo.buildName)
        assertEquals("11", deviceInfo.osVersion)
        assertEquals("test_manufacturer", deviceInfo.manufacturer)
        assertEquals("test_hardware", deviceInfo.hardwareName)
        assertEquals("test_model", deviceInfo.model)
        assertEquals("30", deviceInfo.apiLevel)
        assertEquals("test_brand", deviceInfo.brand)
    }
    
    @Test
    fun testDeviceInfoProperties() {
        val deviceInfo = DeviceInfo(
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
        
        // Test that device info properties are accessible
        assertNotNull(deviceInfo.javaClass.getDeclaredFields())
        assertEquals("android", deviceInfo.osName)
        assertEquals("test_device", deviceInfo.name)
    }
    
    @Test
    fun testDeviceInfoToString() {
        val deviceInfo = DeviceInfo(
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
        val toString = deviceInfo.toString()
        assertNotNull(toString)
        assertTrue(toString.contains("DeviceInfo"))
        assertTrue(toString.contains("android"))
        assertTrue(toString.contains("test_device"))
    }
    
    @Test
    fun testDeviceInfoEquality() {
        val deviceInfo1 = DeviceInfo(
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
        val deviceInfo2 = DeviceInfo(
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
        
        // Should be equal as they have the same values
        assertEquals(deviceInfo1, deviceInfo2)
    }
    
    @Test
    fun testDeviceInfoHashCode() {
        val deviceInfo1 = DeviceInfo(
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
        val deviceInfo2 = DeviceInfo(
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
        
        // Same values should have same hash codes
        assertEquals(deviceInfo1.hashCode(), deviceInfo2.hashCode())
    }
    
    @Test
    fun testDeviceInfoCopy() {
        val original = DeviceInfo(
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
        
        val copy = original.copy(name = "different_device")
        
        assertEquals(original.osName, copy.osName)
        assertEquals("different_device", copy.name)
        assertEquals(original.buildName, copy.buildName)
        assertEquals(original.osVersion, copy.osVersion)
        assertEquals(original.manufacturer, copy.manufacturer)
        assertEquals(original.hardwareName, copy.hardwareName)
        assertEquals(original.model, copy.model)
        assertEquals(original.apiLevel, copy.apiLevel)
        assertEquals(original.brand, copy.brand)
    }
} 