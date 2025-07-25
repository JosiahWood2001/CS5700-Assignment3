package org.example.project

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//stores states for a shipment it's observing
class TrackerViewHelper : Observer {
    var shipmentId by mutableStateOf("")
        private set
    var shipmentStatus by mutableStateOf("Unknown")
        private set
    var shipmentLocation by mutableStateOf("Unknown")
        private set
    var expectedShipmentDeliveryDate by mutableStateOf<String?>(null)
        private set
    var shipmentUpdateHistory = mutableStateListOf<String>()
        private set
    var shipmentNotes = mutableStateListOf<String>()
        private set
    var shipmentType by mutableStateOf("Shipment")
     private set
    //required interface function
    override fun update() {
        val shipment = ShipmentHandler.findShipment(shipmentId)
        shipmentStatus = shipment?.status ?: "Unknown"
        shipmentLocation = shipment?.currentLocation ?: "Unknown"
        if (shipment?.expectedDeliveryDateTimestamp != null) {
            val date = Date(shipment.expectedDeliveryDateTimestamp!!)
            val format = SimpleDateFormat("hh:mm:ss MM/dd/yyyy", Locale.getDefault())
            expectedShipmentDeliveryDate = format.format(date)
        } else {
            expectedShipmentDeliveryDate = null
        }
        shipmentUpdateHistory.clear()
        for (update in shipment?.updateHistory ?: listOf()) {
            val description = update.getString()
            if (description != null) {
                shipmentUpdateHistory.add(description)
            }
        }

        shipmentNotes.clear()
        shipmentNotes.addAll(shipment?.notes ?: emptyList())
        shipmentType = shipment?.type ?: "Shipment"

    }

    //communicate with subject to register or unregister
    fun stopTracking() {
        ShipmentHandler.findShipment(shipmentId)?.removeObserver(this)
    }

    fun trackShipment(id: String) {
        shipmentId = id
        ShipmentHandler.findShipment(shipmentId)?.registerObserver(this)
    }

}