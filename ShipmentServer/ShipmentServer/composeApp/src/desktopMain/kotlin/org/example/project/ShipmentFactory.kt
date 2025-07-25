package org.example.project

interface ShipmentFactory {
    fun createShipment(shipmentId: String): Shipment
}

object ExpressShipmentFactory : ShipmentFactory {
    override fun createShipment(shipmentId: String): Shipment {
        val shipment = Shipment(shipmentId, "Express Shipment", ExpressConditions)
        return shipment
    }
}
object StandardShipmentFactory : ShipmentFactory {
    override fun createShipment(shipmentId: String): Shipment {
        val shipment = Shipment(shipmentId, "Standard Shipment", StandardConditions)
        return shipment
    }
}
object OvernightShipmentFactory : ShipmentFactory {
    override fun createShipment(shipmentId: String): Shipment {
        val shipment = Shipment(shipmentId, "Overnight Shipment", OvernightConditions)
        return shipment
    }
}
object BulkShipmentFactory : ShipmentFactory {
    override fun createShipment(shipmentId: String): Shipment {
        val shipment = Shipment(shipmentId, "Bulk Shipment", BulkConditions)
        return shipment
    }
}