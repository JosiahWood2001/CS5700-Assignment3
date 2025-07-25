package org.example.project

import kotlin.test.*
import org.junit.Test

class ShippingUpdateTest {
    private val initialTimeStamp = 1_689_000_000_000L
    private val updatedTimeStamp = initialTimeStamp + 100_000
    private val initialOther = "ExtraInfo"
    private val updatedOther = "UpdatedExtra"
    private val initialPrevStatus = "processing"
    private val newStatus = "shipped"

    @Test
    fun testConstructorAndGetters() {
        val update = ShippingUpdate(
            Shipped,
            previousStatus = initialPrevStatus,
            newStatus = newStatus,
            timeStamp = initialTimeStamp,
            other = initialOther
        )

        assertEquals(initialPrevStatus, update.getPreviousStatus())
        assertEquals(newStatus, update.getNewStatus())
        assertEquals(initialTimeStamp, update.getTimeStamp())
        assertEquals(initialOther, update.getOther())
    }

    @Test
    fun testSetters() {
        val update = ShippingUpdate(
            Shipped,
            previousStatus = "",
            newStatus = "",
            timeStamp = 0L
        )

        update.setPreviousStatus("in_transit")
        update.setNewStatus("delivered")
        update.setTimeStamp(updatedTimeStamp)
        update.setOther(updatedOther)

        assertEquals("in_transit", update.getPreviousStatus())
        assertEquals("delivered", update.getNewStatus())
        assertEquals(updatedTimeStamp, update.getTimeStamp())
        assertEquals(updatedOther, update.getOther())
    }

    @Test
    fun testGetStringDelegatesToHandler() {
        val update = ShippingUpdate(
            Created,
            previousStatus = "",
            newStatus = "created",
            timeStamp = initialTimeStamp
        )

        val expectedStart = "Shipment created at:"
        val result = update.getString()
        assertNotNull(result)
        assertTrue(result.startsWith(expectedStart), "Expected result to start with: $expectedStart")
    }

    @Test
    fun testGetStringReturnsNullIfHandlerReturnsNull() {
        val update = ShippingUpdate(
            Noteadded,
            previousStatus = "delayed",
            newStatus = "delayed",
            timeStamp = initialTimeStamp,
            other = "Handle with care"
        )

        assertNull(update.getString())
    }
}