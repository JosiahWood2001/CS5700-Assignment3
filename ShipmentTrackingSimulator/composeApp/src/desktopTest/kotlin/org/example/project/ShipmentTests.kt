package org.example.project

import kotlin.test.*

class ShipmentTests {

    private lateinit var shipment: Shipment
    @BeforeTest
    fun setup() {
        shipment = Shipment("ABC123")
    }

    @Test
    fun testInitialValues() {
        assertEquals("ABC123", shipment.getId())
        assertEquals("", shipment.status)
        assertEquals("Unknown", shipment.currentLocation)
        assertTrue(shipment.notes.isEmpty())
        assertTrue(shipment.updateHistory.isEmpty())
        assertNull(shipment.expectedDeliveryDateTimestamp)
    }

    @Test
    fun testSetStatus() {
        var wasUpdated = false
        val observer = object : Observer {
            override fun update(){
                wasUpdated = true
            } }
        shipment.registerObserver(observer)

        shipment.setStatus("shipped")

        assertEquals("shipped", shipment.status)
        assertTrue(wasUpdated)
    }

    @Test
    fun testSetNotes() {
        val notes = mutableListOf("Fragile", "Leave at door")
        var wasUpdated = false
        val observer = object : Observer {
            override fun update(){
                wasUpdated = true
            } }
        shipment.registerObserver(observer)

        shipment.setNotes(notes)

        assertEquals(notes, shipment.notes)
        assertTrue(wasUpdated)
    }

    @Test
    fun testAddNote() {
        var wasUpdated = false
        val observer = object : Observer {
            override fun update(){
                wasUpdated = true
            } }
        shipment.registerObserver(observer)

        shipment.addNote("Handle with care")

        assertTrue(shipment.notes.contains("Handle with care"))
        assertTrue(wasUpdated)
    }

    @Test
    fun testSetUpdateHistory() {
        val update = ShippingUpdate(Created,"created", "delayed", 1234567890L)
        val updates = mutableListOf(update)

        var wasUpdated = false
        val observer = object : Observer {
            override fun update(){
                wasUpdated = true
            } }
        shipment.registerObserver(observer)

        shipment.setUpdateHistory(updates)

        assertEquals(updates, shipment.updateHistory)
        assertTrue(wasUpdated)
    }

    @Test
    fun testAddUpdate() {
        val update = ShippingUpdate(Created,"created", "delayed", 1234567890L)
        var wasUpdated = false
        val observer = object : Observer {
            override fun update(){
                wasUpdated = true
            } }
        shipment.registerObserver(observer)

        shipment.addUpdate(update)

        assertEquals(1, shipment.updateHistory.size)
        assertTrue(shipment.updateHistory.contains(update))
        assertTrue(wasUpdated)
    }

    @Test
    fun testSetExpectedDeliveryDateTimestamp() {
        val timestamp = 1650000000000L
        var wasUpdated = false
        val observer = object : Observer {
            override fun update(){
                wasUpdated = true
            } }
        shipment.registerObserver(observer)

        shipment.setExpectedDeliveryDateTimestamp(timestamp)

        assertEquals(timestamp, shipment.expectedDeliveryDateTimestamp)
        assertTrue(wasUpdated)
    }

    @Test
    fun testSetCurrentLocation() {
        var wasUpdated = false
        val observer = object : Observer {
            override fun update(){
                wasUpdated = true
            } }
        shipment.registerObserver(observer)

        shipment.setCurrentLocation("Dallas, TX")

        assertEquals("Dallas, TX", shipment.currentLocation)
        assertTrue(wasUpdated)
    }

    @Test
    fun testObserverRegistrationAndRemoval() {
        var updateCount = 0
        val observer = object : Observer {
            override fun update(){
                updateCount++
            } }
        shipment.registerObserver(observer)

        shipment.registerObserver(observer)
        shipment.setStatus("shipped") // should trigger update

        assertEquals(1, updateCount)

        shipment.removeObserver(observer)
        shipment.setStatus("delivered") // should NOT trigger

        assertEquals(1, updateCount)
    }
}