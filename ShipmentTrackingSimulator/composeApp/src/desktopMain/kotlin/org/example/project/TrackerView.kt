package org.example.project

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier

//trackerView is the UI for trackerviewHelper to get information from a shipment

@Composable
fun TrackerView(tracker: TrackerViewHelper) {
    //if the shipment doesn't exist it will show that then return - skipping all other info
    if (tracker.shipmentStatus == "Unknown") {
        Column(modifier = Modifier.padding(top = 8.dp)) {
            Text("Shipment not found.")
            Button(onClick = {
                tracker.trackShipment(tracker.shipmentId)
                tracker.update()
            }) { Text("Check Again") }
        }
        return
    }
    Column(modifier = Modifier.padding(top = 8.dp)) {
        Text("Status: ${tracker.shipmentStatus}", fontWeight = FontWeight.SemiBold)
        Text("Location: ${tracker.shipmentLocation}")
        Text("Expected Delivery: ${tracker.expectedShipmentDeliveryDate ?: "--"}")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Status Updates:", fontWeight = FontWeight.SemiBold)
        //this will not occur while the create sends a message, but this is something that's flexible
        if (tracker.shipmentUpdateHistory.isEmpty()) {
            Text("No updates yet.")
        } else {
            Column {
                tracker.shipmentUpdateHistory.forEach { update ->
                    Text("• $update")
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text("Notes:", fontWeight = FontWeight.SemiBold)
        if (tracker.shipmentNotes.isEmpty()) {
            Text("No notes.")
        } else {
            Column {
                tracker.shipmentNotes.forEach { note ->
                    Text("• $note")
                }
            }
        }
    }
}