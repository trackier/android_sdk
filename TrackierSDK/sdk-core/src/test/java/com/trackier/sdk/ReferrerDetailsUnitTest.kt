package com.trackier.sdk.unit

import com.trackier.sdk.RefererDetails
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.After

class RefererDetailsUnitTest {
    @Before
    fun setUp() {}
    @After
    fun tearDown() {}

    @Test
    fun testRefererDetailsConstruction() {
        val details = RefererDetails("test_url", "test_click", "test_install")
        assertNotNull(details)
        assertEquals("test_url", details.url)
        assertEquals("test_click", details.clickTime)
        assertEquals("test_install", details.installTime)
    }

    @Test
    fun testRefererDetailsWithEmptyValues() {
        val details = RefererDetails("", "", "")
        assertEquals("", details.url)
        assertEquals("", details.clickTime)
        assertEquals("", details.installTime)
    }

    @Test
    fun testRefererDetailsDataClassEquality() {
        val details1 = RefererDetails("url1", "click1", "install1")
        val details2 = RefererDetails("url1", "click1", "install1")
        val details3 = RefererDetails("url2", "click1", "install1")
        assertEquals(details1, details2)
        assertNotEquals(details1, details3)
    }

    @Test
    fun testRefererDetailsToString() {
        val details = RefererDetails("test_url", "test_click", "test_install")
        val toString = details.toString()
        assertTrue(toString.contains("test_url"))
        assertTrue(toString.contains("test_click"))
        assertTrue(toString.contains("test_install"))
    }
} 