package com.trackier.sdk.unit

import com.trackier.sdk.ResponseData
import com.trackier.sdk.DlData
import org.junit.Assert.*
import org.junit.Test

class ResponseDataUnitTest {
    @Test
    fun `test ResponseData creation and property assignment`() {
        val dlData = DlData(url = "https://example.com", dlv = "abc", sdkParams = mutableMapOf("foo" to 1))
        val response = ResponseData(
            success = true,
            id = "id123",
            ad = "ad1",
            adId = "adid1",
            camp = "camp1",
            campId = "campid1",
            adSet = "adset1",
            adSetId = "adsetid1",
            channel = "channel1",
            p1 = "p1",
            p2 = "p2",
            p3 = "p3",
            p4 = "p4",
            p5 = "p5",
            clickId = "clickid1",
            dlv = "dlv1",
            pid = "pid1",
            partner = "partner1",
            isRetargeting = false,
            message = "ok",
            data = dlData
        )
        assertTrue(response.success)
        assertEquals("id123", response.id)
        assertEquals("ad1", response.ad)
        assertEquals("adid1", response.adId)
        assertEquals("camp1", response.camp)
        assertEquals("campid1", response.campId)
        assertEquals("adset1", response.adSet)
        assertEquals("adsetid1", response.adSetId)
        assertEquals("channel1", response.channel)
        assertEquals("p1", response.p1)
        assertEquals("p2", response.p2)
        assertEquals("p3", response.p3)
        assertEquals("p4", response.p4)
        assertEquals("p5", response.p5)
        assertEquals("clickid1", response.clickId)
        assertEquals("dlv1", response.dlv)
        assertEquals("pid1", response.pid)
        assertEquals("partner1", response.partner)
        assertFalse(response.isRetargeting!!)
        assertEquals("ok", response.message)
        assertNotNull(response.data)
        assertEquals("https://example.com", response.data?.url)
        assertEquals("abc", response.data?.dlv)
        assertEquals(1, response.data?.sdkParams?.get("foo"))
    }
} 