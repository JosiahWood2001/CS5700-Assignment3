package org.example.project

import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals
import kotlin.test.Test

class ShipmentFactoryTests {

    @Test
    fun `ExpressShipmentFactory should create correct shipment`() {
        val shipment = assertDoesNotThrow {
            ExpressShipmentFactory.createShipment("EXP123")
        }
        assertEquals("EXP123", shipment.getId())
        assertEquals("Express Shipment", shipment.type)
        assertEquals(ExpressConditions, shipment.getConditions())
    }

    @Test
    fun `StandardShipmentFactory should create correct shipment`() {
        val shipment = assertDoesNotThrow {
            StandardShipmentFactory.createShipment("STD456")
        }
        assertEquals("STD456", shipment.getId())
        assertEquals("Standard Shipment", shipment.type)
        assertEquals(StandardConditions, shipment.getConditions())
    }

    @Test
    fun `OvernightShipmentFactory should create correct shipment`() {
        val shipment = assertDoesNotThrow {
            OvernightShipmentFactory.createShipment("ON789")
        }
        assertEquals("ON789", shipment.getId())
        assertEquals("Overnight Shipment", shipment.type)
        assertEquals(OvernightConditions, shipment.getConditions())
    }

    @Test
    fun `BulkShipmentFactory should create correct shipment`() {
        val shipment = assertDoesNotThrow {
            BulkShipmentFactory.createShipment("BULK321")
        }
        assertEquals("BULK321", shipment.getId())
        assertEquals("Bulk Shipment", shipment.type)
        assertEquals(BulkConditions, shipment.getConditions())
    }
}