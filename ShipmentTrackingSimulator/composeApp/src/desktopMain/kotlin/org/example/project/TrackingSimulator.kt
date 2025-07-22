package org.example.project

import kotlinx.coroutines.delay
import java.io.File

//dict to locate updateHandlers
val handlers: Map<String, UpdateHandler> = mapOf(
    Created.TYPE to Created, Delayed.TYPE to Delayed, Shipped.TYPE to Shipped, Lost.TYPE to Lost,
    Location.TYPE to Location, Canceled.TYPE to Canceled, Delivered.TYPE to Delivered, Noteadded.TYPE to Noteadded
)

object TrackingSimulator {
    //stores all shipments
    private var shipments = mutableSetOf<Shipment>()
    fun findShipment(id: String): Shipment? {
        return shipments.find { it.getId() == id }
    }

    fun addShipment(shipment: Shipment) {
        shipments.add(shipment)
    }

    //is executed to read the file
    suspend fun runSimulation() {
        File("test.txt").useLines { lines ->
            for (line in lines) {
                delay(1000)
                val lineElements = line.split(",")
                //pass all elements to the respective handler(if it exists)
                handlers[lineElements[0]]?.handleUpdate(lineElements)
            }
        }
    }
}