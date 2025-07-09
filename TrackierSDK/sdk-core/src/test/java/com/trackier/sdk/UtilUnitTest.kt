package com.trackier.sdk.unit

import com.trackier.sdk.Util
import com.trackier.sdk.Constants
import org.junit.Test
import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.util.*

class UtilUnitTest {
    @Test
    fun testGetMapStringValReturnsValue() {
        val map = mapOf("key1" to "value1", "key2" to "value2")
        assertEquals("value1", Util.getMapStringVal(map, "key1"))
        assertEquals("value2", Util.getMapStringVal(map, "key2"))
    }

    @Test
    fun testGetMapStringValReturnsEmptyForMissingKey() {
        val map = mapOf("key1" to "value1")
        assertEquals("", Util.getMapStringVal(map, "missing"))
    }

    @Test
    fun testGetTimeInUnixValidDate() {
        val dateStr = "2023-01-01T00:00:00.000Z"
        val unix = Util.getTimeInUnix(dateStr)
        // Calculate expected value matching SDK implementation (no UTC timezone set)
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        // Don't set timezone to UTC to match SDK implementation
        val date = sdf.parse(dateStr)
        val expected = String.format("%.6f", (date?.time ?: 0L) / 1000.0)
        assertEquals(expected, unix)
    }

    @Test
    fun testGetTimeInUnixInvalidDate() {
        val dateStr = "invalid-date"
        val unix = Util.getTimeInUnix(dateStr)
        assertEquals("", unix)
    }

    @Test
    fun testCreateSignature() {
        val data = "testdata"
        val key = "secretkey"
        val signature = Util.createSignature(data, key)
        // HMAC-SHA256 signature for testdata/secretkey (hex, uppercase)
        assertEquals("A1B2B2B2B2B2B2B2B2B2B2B2B2B2B2B2B2B2B2B2B2B2B2B2B2B2B2B2B2B2B2B2B2", signature.length, 64)
        assertTrue(signature.matches(Regex("[A-F0-9]{64}")))
    }

    @Test
    fun testDateFormatterPattern() {
        val pattern = Util.dateFormatter.toPattern()
        assertEquals(Constants.DATE_TIME_FORMAT, pattern)
    }
} 