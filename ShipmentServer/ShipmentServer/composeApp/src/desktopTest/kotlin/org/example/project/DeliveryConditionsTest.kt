package org.example.project

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

private const val ONE_DAY_MS = 86_400_000L
private const val THREE_DAYS_MS = ONE_DAY_MS * 3

class DeliveryConditionsTest {

    private val now = System.currentTimeMillis()

    @Test
    fun `Express shipment within 3 days does not raise warning`() {
        val shipment = Shipment("EXP1", "Express", ExpressConditions)
        shipment.setCreatedTimeStamp(now)
        shipment.setExpectedDeliveryDateTimestamp(now + THREE_DAYS_MS)

        assertNotEquals("!Something went wrong!", shipment.status)
        assertTrue(shipment.notes.isEmpty())
    }

    @Test
    fun `Express shipment more than 3 days raises warning`() {
        val shipment = Shipment("EXP2", "Express", ExpressConditions)
        shipment.setCreatedTimeStamp(now)
        shipment.setExpectedDeliveryDateTimestamp(now + THREE_DAYS_MS + 1)

        assertEquals("!Something went wrong!", shipment.status)
        assertTrue(shipment.notes.any { it.contains("Express Shipment") })
    }

    @Test
    fun `Overnight shipment within 1 day does not raise warning`() {
        val shipment = Shipment("ON1", "Overnight", OvernightConditions)
        shipment.setCreatedTimeStamp(now)
        shipment.setExpectedDeliveryDateTimestamp(now + ONE_DAY_MS)

        assertNotEquals("!Something went wrong!", shipment.status)
        assertTrue(shipment.notes.isEmpty())
    }

    @Test
    fun `Overnight shipment more than 1 day raises warning`() {
        val shipment = Shipment("ON2", "Overnight", OvernightConditions)
        shipment.setCreatedTimeStamp(now)
        shipment.setExpectedDeliveryDateTimestamp(now + ONE_DAY_MS + 1)

        assertEquals("!Something went wrong!", shipment.status)
        assertTrue(shipment.notes.any { it.contains("Overnight Shipment") })
    }

    @Test
    fun `Bulk shipment sooner than 3 days raises warning`() {
        val shipment = Shipment("BULK1", "Bulk", BulkConditions)
        shipment.setCreatedTimeStamp(now)
        shipment.setExpectedDeliveryDateTimestamp(now + THREE_DAYS_MS - 1)

        assertEquals("!Something went wrong!", shipment.status)
        assertTrue(shipment.notes.any { it.contains("Bulk Shipment") })
    }

    @Test
    fun `Bulk shipment in 3 days or more is fine`() {
        val shipment = Shipment("BULK2", "Bulk", BulkConditions)
        shipment.setCreatedTimeStamp(now)
        shipment.setExpectedDeliveryDateTimestamp(now + THREE_DAYS_MS)

        assertNotEquals("!Something went wrong!", shipment.status)
        assertTrue(shipment.notes.isEmpty())
    }

    @Test
    fun `Standard shipment has no condition enforcement`() {
        val shipment = Shipment("STD1", "Standard", StandardConditions)
        shipment.setCreatedTimeStamp(now)
        shipment.setExpectedDeliveryDateTimestamp(now + 100000000L)

        assertTrue(shipment.notes.isEmpty())
        assertNotEquals("!Something went wrong!", shipment.status)
    }

    @Test
    fun `No exception thrown when setting valid data`() {
        val shipment = Shipment("SAFE1", "Standard", StandardConditions)

        assertDoesNotThrow {
            shipment.setCreatedTimeStamp(now)
            shipment.setExpectedDeliveryDateTimestamp(now + 100000L)
        }
    }
}