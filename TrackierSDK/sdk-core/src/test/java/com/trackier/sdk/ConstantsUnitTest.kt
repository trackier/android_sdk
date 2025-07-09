package com.trackier.sdk.unit

import com.trackier.sdk.Constants
import org.junit.Test
import org.junit.Assert.*

class ConstantsUnitTest {
    @Test
    fun testSdkVersionConstant() {
        // Update this if SDK_VERSION changes in Constants.kt
        assertEquals("1.6.73", Constants.SDK_VERSION)
    }

    @Test
    fun testBaseUrlConstant() {
        // The base URL should match the expected value in Constants.kt
        assertEquals("events.trackier.io/v1/", Constants.BASE_URL)
    }

    @Test
    fun testEmptyUUID() {
        assertEquals("00000000-0000-0000-0000-000000000000", Constants.UUID_EMPTY)
    }
} 