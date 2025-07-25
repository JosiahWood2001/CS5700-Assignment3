package org.example.project

import kotlin.test.*
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import org.junit.jupiter.api.assertDoesNotThrow

class UpdateHandlerTest {
    val trackingId = "SHIP123"
    val baseTimestamp = System.currentTimeMillis()
    private val timestamp = 1_689_000_000_000 // arbitrary fixed timestamp
    private val otherTimestamp = timestamp + 100000
    private val dateFormat = SimpleDateFormat("hh:mm:ss MM/dd/yyyy", Locale.getDefault())
    private val fakeTrackingId = "nonexistent123"
    private fun format(time: Long): String = dateFormat.format(Date(time))

    @Before
    fun setUp() {
        val privateShipments = ShipmentHandler.javaClass.getDeclaredField("shipments")
        privateShipments.isAccessible = true
        val shipments = privateShipments.get(ShipmentHandler)
        when (shipments) {
            is MutableList<*> -> shipments.clear()
            is MutableSet<*> -> shipments.clear()
            else -> error("Unsupported type: ${shipments?.javaClass}")
        }
    }

    @Test
    fun testCreatedUpdate() {
        Created.handleUpdate(listOf("created", trackingId, "standard", baseTimestamp.toString()))
        val shipment = ShipmentHandler.findShipment(trackingId)
        assertNotNull(shipment)
        assertEquals("created", shipment.status)
        assertEquals(1, shipment.updateHistory.size)
        assertTrue(shipment.updateHistory.first().getString()?.contains("created") ?: false )
    }

    @Test
    fun testDelayedUpdate() {
        testCreatedUpdate()

        val newDate = baseTimestamp + 100_000
        val updateTime = baseTimestamp + 5_000

        Delayed.handleUpdate(listOf("delayed", trackingId, updateTime.toString(), newDate.toString()))

        val shipment = ShipmentHandler.findShipment(trackingId)

        assertEquals("delayed", shipment?.status)
        assertEquals(2, shipment?.updateHistory?.size)  // check history count

        val actualOther = shipment?.updateHistory?.lastOrNull()?.getOther()?.toLong()
        assertEquals(newDate, actualOther)  // compare timestamps as Long
    }

    @Test
    fun testShippedUpdate() {
        testCreatedUpdate()
        val newDate = baseTimestamp + 100000
        Shipped.handleUpdate(listOf("shipped", trackingId, (baseTimestamp + 10000).toString(), newDate.toString()))
        val shipment = ShipmentHandler.findShipment(trackingId)
        assertEquals("shipped", shipment?.status)
        assertEquals(2, shipment?.updateHistory?.size)
        val latestUpdate = shipment?.updateHistory?.last()
        assertEquals(newDate, latestUpdate?.getOther()?.toLong())
        assertEquals(newDate, shipment?.expectedDeliveryDateTimestamp)
    }

    @Test
    fun testLostUpdate() {
        testCreatedUpdate()
        Lost.handleUpdate(listOf("lost", trackingId, (baseTimestamp + 20000).toString()))
        val shipment = ShipmentHandler.findShipment(trackingId)
        assertEquals("lost", shipment?.status)
        assertEquals(2, shipment?.updateHistory?.size)
    }

    @Test
    fun testLocationUpdate() {
        testCreatedUpdate()
        val location = "Chicago, IL"
        Location.handleUpdate(listOf("location", trackingId, (baseTimestamp + 15000).toString(), location))
        val shipment = ShipmentHandler.findShipment(trackingId)
        assertEquals(location, shipment?.currentLocation)
        assertEquals(2, shipment?.updateHistory?.size)
    }

    @Test
    fun testCanceledUpdate() {
        testCreatedUpdate()
        Canceled.handleUpdate(listOf("canceled", trackingId, (baseTimestamp + 18000).toString()))
        val shipment = ShipmentHandler.findShipment(trackingId)
        assertEquals("canceled", shipment?.status)
        assertEquals(2, shipment?.updateHistory?.size)
    }

    @Test
    fun testDeliveredUpdate() {
        testCreatedUpdate()
        Delivered.handleUpdate(listOf("delivered", trackingId, (baseTimestamp + 22000).toString()))
        val shipment = ShipmentHandler.findShipment(trackingId)
        assertEquals("delivered", shipment?.status)
        assertEquals(2, shipment?.updateHistory?.size)
    }

    @Test
    fun testNoteAddedUpdate() {
        testCreatedUpdate()
        val note = "Handle with care"
        Noteadded.handleUpdate(listOf("noteadded", trackingId, (baseTimestamp + 25000).toString(), note))
        val shipment = ShipmentHandler.findShipment(trackingId)
        assertEquals(2, shipment?.updateHistory?.size)
    }

    @Test
    fun testCreatedReport() {
        val update = ShippingUpdate(Created, "", "created", timestamp)
        val expected = "Shipment created at: ${format(timestamp)}"
        assertEquals(expected, Created.reportUpdate(update))
    }

    @Test
    fun testDelayedReport() {
        val update = ShippingUpdate(Delayed, "", "delayed", timestamp, otherTimestamp.toString())
        val expected = "Shipment was delayed at: ${format(timestamp)}, new Expected Delivery: ${format(otherTimestamp)}"
        assertEquals(expected, Delayed.reportUpdate(update))
    }

    @Test
    fun testShippedReport() {
        val update = ShippingUpdate(Shipped, "", "shipped", timestamp, otherTimestamp.toString())
        val expected = "Shipment was shipped at: ${format(timestamp)}"
        assertEquals(expected, Shipped.reportUpdate(update))
    }

    @Test
    fun testLostReport() {
        val update = ShippingUpdate(Lost, "", "lost", timestamp)
        val expected = "Shipment was lost at: ${format(timestamp)}"
        assertEquals(expected, Lost.reportUpdate(update))
    }

    @Test
    fun testLocationReport() {
        val location = "Denver, CO"
        val update = ShippingUpdate(Location, "", "delayed", timestamp, location)
        val expected = "Shipment arrived to $location at: ${format(timestamp)}"
        assertEquals(expected, Location.reportUpdate(update))
    }

    @Test
    fun testCanceledReport() {
        val update = ShippingUpdate(Canceled, "", "canceled", timestamp)
        val expected = "Shipment was cancelled by customer at: ${format(timestamp)}"
        assertEquals(expected, Canceled.reportUpdate(update))
    }

    @Test
    fun testDeliveredReport() {
        val update = ShippingUpdate(Delivered, "", "delivered", timestamp)
        val expected = "Shipment was delivered at: ${format(timestamp)}"
        assertEquals(expected, Delivered.reportUpdate(update))
    }

    @Test
    fun testNoteAddedReportIsNull() {
        val update = ShippingUpdate(Noteadded, "", "delayed", timestamp, "Fragile - glass")
        assertNull(Noteadded.reportUpdate(update))
    }
    @Test
    fun testAllHandlersWhenShipmentIsNull() {
        assertDoesNotThrow {
            Delayed.handleUpdate(listOf("delayed", fakeTrackingId, timestamp.toString(), otherTimestamp.toString()))
            Shipped.handleUpdate(listOf("shipped", fakeTrackingId, timestamp.toString(), otherTimestamp.toString()))
            Lost.handleUpdate(listOf("lost", fakeTrackingId, timestamp.toString()))
            Location.handleUpdate(listOf("location", fakeTrackingId, timestamp.toString(), "Chicago"))
            Canceled.handleUpdate(listOf("canceled", fakeTrackingId, timestamp.toString()))
            Delivered.handleUpdate(listOf("delivered", fakeTrackingId, timestamp.toString()))
            Noteadded.handleUpdate(listOf("noteadded", fakeTrackingId, timestamp.toString(), "Urgent delivery"))
        }

        // Verify no shipment was added unexpectedly
        val result = ShipmentHandler.findShipment(fakeTrackingId)
        assertNull(result, "No shipment should exist with fake ID")
    }

}