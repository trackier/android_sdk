package com.trackier.sdk.unit

import com.trackier.sdk.InstallReferrerException
import org.junit.Assert.*
import org.junit.Test

class InstallReferrerExceptionUnitTest {

    @Test
    fun `test InstallReferrerException with message`() {
        val message = "Test exception message"
        val exception = InstallReferrerException(message)
        
        assertEquals(message, exception.message)
        assertTrue(exception is Exception)
    }

    @Test
    fun `test InstallReferrerException inheritance`() {
        val exception = InstallReferrerException("Test")
        
        // InstallReferrerException extends Exception, not RuntimeException
        assertTrue(exception is Exception)
    }

    @Test
    fun `test InstallReferrerException can be thrown and caught`() {
        val message = "Custom error message"
        
        try {
            throw InstallReferrerException(message)
            fail("Exception should have been thrown")
        } catch (e: InstallReferrerException) {
            assertEquals(message, e.message)
        } catch (e: Exception) {
            // This should not be reached, but if it is, the test should still pass
            assertTrue(e is InstallReferrerException)
        }
    }
} 