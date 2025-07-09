package com.trackier.sdk.unit

import com.trackier.sdk.AttributionParams
import org.junit.Assert.*
import org.junit.Test

class AttributionParamsUnitTest {
    @Test
    fun `test AttributionParams creation`() {
        val params = AttributionParams()
        assertNotNull(params)
    }

    @Test
    fun `test AttributionParams with default constructor`() {
        val params = AttributionParams()
        // Test that object is created successfully
        assertNotNull(params)
    }
} 