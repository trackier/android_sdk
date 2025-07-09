package com.trackier.sdk.unit

import com.trackier.sdk.XiaomiReferrerDetails
import org.junit.Assert.*
import org.junit.Test

class XiaomiReferrerDetailsUnitTest {

    @Test
    fun `test XiaomiReferrerDetails initialization`() {
        val referrer = "test_referrer_string"
        val details = XiaomiReferrerDetails(referrer)
        
        assertEquals(referrer, details.installReferrer)
        assertEquals(-1, details.referrerClickTimestampSeconds)
        assertEquals(-1, details.installBeginTimestampSeconds)
    }

    @Test
    fun `test XiaomiReferrerDetails with all parameters`() {
        val referrer = "test_referrer"
        val clickTimestamp = 1234567890
        val installTimestamp = 1234567891
        
        val details = XiaomiReferrerDetails(referrer, clickTimestamp, installTimestamp)
        
        assertEquals(referrer, details.installReferrer)
        assertEquals(clickTimestamp, details.referrerClickTimestampSeconds)
        assertEquals(installTimestamp, details.installBeginTimestampSeconds)
    }

    @Test
    fun `test default XiaomiReferrerDetails`() {
        val defaultDetails = XiaomiReferrerDetails.default()
        
        assertEquals(XiaomiReferrerDetails.ORGANIC_REF, defaultDetails.installReferrer)
        assertEquals(-1, defaultDetails.referrerClickTimestampSeconds)
        assertEquals(-1, defaultDetails.installBeginTimestampSeconds)
    }

    @Test
    fun `test ORGANIC_REF constant`() {
        assertEquals("utm_source=organic", XiaomiReferrerDetails.ORGANIC_REF)
    }

    @Test
    fun `test data class properties are mutable`() {
        val details = XiaomiReferrerDetails("initial")
        
        details.installReferrer = "updated"
        details.referrerClickTimestampSeconds = 100
        details.installBeginTimestampSeconds = 200
        
        assertEquals("updated", details.installReferrer)
        assertEquals(100, details.referrerClickTimestampSeconds)
        assertEquals(200, details.installBeginTimestampSeconds)
    }
} 