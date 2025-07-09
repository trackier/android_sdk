package com.trackier.sdk.unit

import com.trackier.sdk.TrackierEvent
import org.junit.Assert.*
import org.junit.Test

class TrackierEventUnitTest {
    @Test
    fun `test TrackierEvent creation with id only`() {
        val event = TrackierEvent("event_id")
        assertEquals("event_id", event.id)
        assertNull(event.orderId)
        assertNull(event.productId)
        assertNull(event.currency)
    }

    @Test
    fun `test TrackierEvent property assignment`() {
        val event = TrackierEvent("event_id")
        event.orderId = "order_1"
        event.productId = "product_1"
        event.currency = "USD"
        event.revenue = 10.0
        event.discount = 2.0f
        event.couponCode = "COUPON"
        event.param1 = "p1"
        event.param2 = "p2"
        event.param3 = "p3"
        event.ev["custom"] = 123
        assertEquals("order_1", event.orderId)
        assertEquals("product_1", event.productId)
        assertEquals("USD", event.currency)
        assertNotNull(event.revenue)
        assertEquals(10.0, event.revenue!!, 0.01)
        assertNotNull(event.discount)
        assertEquals(2.0f, event.discount!!, 0.01f)
        assertEquals("COUPON", event.couponCode)
        assertEquals("p1", event.param1)
        assertEquals("p2", event.param2)
        assertEquals("p3", event.param3)
        assertEquals(123, event.ev["custom"])
    }

    @Test
    fun `test TrackierEvent constants`() {
        assertEquals("1CFfUn3xEY", TrackierEvent.LEVEL_ACHIEVED)
        assertEquals("Fy4uC1_FlN", TrackierEvent.ADD_TO_CART)
        assertEquals("AOisVC76YG", TrackierEvent.ADD_TO_WISHLIST)
        assertEquals("mEqP4aD8dU", TrackierEvent.COMPLETE_REGISTRATION)
        assertEquals("99VEGvXjN7", TrackierEvent.TUTORIAL_COMPLETION)
        assertEquals("Q4YsqBKnzZ", TrackierEvent.PURCHASE)
        assertEquals("B4N_In4cIP", TrackierEvent.SUBSCRIBE)
        assertEquals("jYHcuyxWUW", TrackierEvent.START_TRIAL)
        assertEquals("xTPvxWuNqm", TrackierEvent.ACHIEVEMENT_UNLOCKED)
        assertEquals("Jwzois1ays", TrackierEvent.CONTENT_VIEW)
        assertEquals("yP1-ipVtHV", TrackierEvent.TRAVEL_BOOKING)
        assertEquals("dxZXGG1qqL", TrackierEvent.SHARE)
        assertEquals("7lnE3OclNT", TrackierEvent.INVITE)
        assertEquals("o91gt1Q0PK", TrackierEvent.LOGIN)
        assertEquals("sEQWVHGThl", TrackierEvent.UPDATE)
    }
} 