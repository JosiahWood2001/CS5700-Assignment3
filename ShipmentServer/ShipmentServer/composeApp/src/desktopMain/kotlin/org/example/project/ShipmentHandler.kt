package org.example.project

val handlers: Map<String, UpdateHandler> = mapOf(
    Created.TYPE to Created, Delayed.TYPE to Delayed, Shipped.TYPE to Shipped, Lost.TYPE to Lost,
    Location.TYPE to Location, Canceled.TYPE to Canceled, Delivered.TYPE to Delivered, Noteadded.TYPE to Noteadded
)
object ShipmentHandler {
    private var shipments = mutableSetOf<Shipment>()
    fun findShipment(id: String): Shipment? {
        return shipments.find { it.getId() == id }
    }

    fun addShipment(shipment: Shipment) {
        shipments.add(shipment)
        findShipment(shipment.getId())
    }
    fun receiveUpdateRequest(update: String) {
        val lineElements = update.split(",")
        handlers[lineElements[0]]?.handleUpdate(lineElements)
    }
}