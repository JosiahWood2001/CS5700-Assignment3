package org.example.project

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val shipmentTypes: Map<String, ShipmentFactory> = mapOf(
    "express" to ExpressShipmentFactory, "bulk" to BulkShipmentFactory,
    "standard" to StandardShipmentFactory, "overnight" to OvernightShipmentFactory,
)
//each handler will perform the update immediately, then provide a function for providing status text later
interface UpdateHandler {
    fun handleUpdate(updateString: List<String>)
    fun reportUpdate(update: ShippingUpdate): String?
}

//all are objects since they are all static
object Created : UpdateHandler {
    const val TYPE: String = "created"
    override fun handleUpdate(updateString: List<String>) {
        val shipment = shipmentTypes[updateString[2]]?.createShipment(updateString[1])
        val update = ShippingUpdate(this, "", "created", updateString[3].toLong())
        shipment?.setStatus("created")
        shipment?.addUpdate(update)
        shipment?.setCreatedTimeStamp(updateString[3].toLong())
        ShipmentHandler.addShipment(shipment)
    }

    override fun reportUpdate(update: ShippingUpdate): String? {
        val date = Date(update.getTimeStamp())
        val format = SimpleDateFormat("hh:mm:ss MM/dd/yyyy", Locale.getDefault())
        return "Shipment created at: ${format.format(date)}"
    }
}

object Delayed : UpdateHandler {
    const val TYPE: String = "delayed"
    override fun handleUpdate(updateString: List<String>) {
        val shipment = ShipmentHandler.findShipment(updateString[1])
        val update = ShippingUpdate(this, shipment?.status ?: "", "delayed", updateString[2].toLong(), updateString[3])
        shipment?.setStatus("delayed")
        shipment?.addUpdate(update)
        shipment?.setExpectedDeliveryDateTimestamp(update.getOther().toLong(),false)
    }

    override fun reportUpdate(update: ShippingUpdate): String? {
        val date = Date(update.getTimeStamp())
        val newDate = Date(update.getOther().toLong())
        val format = SimpleDateFormat("hh:mm:ss MM/dd/yyyy", Locale.getDefault())
        return "Shipment was delayed at: ${format.format(date)}, new Expected Delivery: ${format.format(newDate)}"
    }
}

object Shipped : UpdateHandler {
    const val TYPE: String = "shipped"
    override fun handleUpdate(updateString: List<String>) {
        val shipment = ShipmentHandler.findShipment(updateString[1])
        val update = ShippingUpdate(this, shipment?.status ?: "", "shipped", updateString[2].toLong(), updateString[3])
        shipment?.setStatus("shipped")
        shipment?.addUpdate(update)
        shipment?.setExpectedDeliveryDateTimestamp(update.getOther().toLong())
    }

    override fun reportUpdate(update: ShippingUpdate): String? {
        val date = Date(update.getTimeStamp())
        val format = SimpleDateFormat("hh:mm:ss MM/dd/yyyy", Locale.getDefault())
        return "Shipment was shipped at: ${format.format(date)}"
    }
}

object Lost : UpdateHandler {
    const val TYPE: String = "lost"
    override fun handleUpdate(updateString: List<String>) {
        val shipment = ShipmentHandler.findShipment(updateString[1])
        val update = ShippingUpdate(this, shipment?.status ?: "", "lost", updateString[2].toLong())
        shipment?.setStatus("lost")
        shipment?.addUpdate(update)
    }

    override fun reportUpdate(update: ShippingUpdate): String? {
        val date = Date(update.getTimeStamp())
        val format = SimpleDateFormat("hh:mm:ss MM/dd/yyyy", Locale.getDefault())
        return "Shipment was lost at: ${format.format(date)}"
    }
}

object Location : UpdateHandler {
    const val TYPE: String = "location"
    override fun handleUpdate(updateString: List<String>) {
        val shipment = ShipmentHandler.findShipment(updateString[1])
        val update = ShippingUpdate(this, shipment?.status ?: "", "delayed", updateString[2].toLong(), updateString[3])
        shipment?.setCurrentLocation(updateString[3])
        shipment?.addUpdate(update)
    }

    override fun reportUpdate(update: ShippingUpdate): String? {
        val date = Date(update.getTimeStamp())
        val format = SimpleDateFormat("hh:mm:ss MM/dd/yyyy", Locale.getDefault())
        return "Shipment arrived to ${update.getOther()} at: ${format.format(date)}"
    }
}

object Canceled : UpdateHandler {
    const val TYPE: String = "canceled"
    override fun handleUpdate(updateString: List<String>) {
        val shipment = ShipmentHandler.findShipment(updateString[1])
        val update = ShippingUpdate(this, shipment?.status ?: "", "canceled", updateString[2].toLong())
        shipment?.setStatus("canceled")
        shipment?.addUpdate(update)
    }

    override fun reportUpdate(update: ShippingUpdate): String? {
        val date = Date(update.getTimeStamp())
        val format = SimpleDateFormat("hh:mm:ss MM/dd/yyyy", Locale.getDefault())
        return "Shipment was cancelled by customer at: ${format.format(date)}"
    }
}

object Delivered : UpdateHandler {
    const val TYPE: String = "delivered"
    override fun handleUpdate(updateString: List<String>) {
        val shipment = ShipmentHandler.findShipment(updateString[1])
        val update = ShippingUpdate(this, shipment?.status ?: "", "delivered", updateString[2].toLong())
        shipment?.setStatus("delivered")
        shipment?.addUpdate(update)
    }

    override fun reportUpdate(update: ShippingUpdate): String? {
        val date = Date(update.getTimeStamp())
        val format = SimpleDateFormat("hh:mm:ss MM/dd/yyyy", Locale.getDefault())
        return "Shipment was delivered at: ${format.format(date)}"
    }
}

object Noteadded : UpdateHandler {
    const val TYPE: String = "noteadded"
    override fun handleUpdate(updateString: List<String>) {
        val shipment = ShipmentHandler.findShipment(updateString[1])
        val update = ShippingUpdate(this, shipment?.status ?: "", "delayed", updateString[2].toLong(), updateString[3])
        shipment?.addNote(updateString[3])
        shipment?.addUpdate(update)
    }

    override fun reportUpdate(update: ShippingUpdate): String? {
        return null
    }
}