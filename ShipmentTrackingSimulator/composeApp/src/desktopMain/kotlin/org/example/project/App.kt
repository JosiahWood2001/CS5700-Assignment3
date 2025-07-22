package org.example.project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val simulator = remember { TrackingSimulator }
    LaunchedEffect(simulator) {
        simulator.runSimulation()
    }
    MaterialTheme {
        //first contains the search bar
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            val trackers = remember { mutableStateListOf<TrackerViewHelper>() }
            var searchField by remember { mutableStateOf("") }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextField(
                    searchField,
                    onValueChange = { searchField = it },
                    label = { Text("Enter Shipment ID") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Button(onClick = {
                    //ensure valid entry, and check to see if it's a duplicate
                    if (searchField.isNotBlank()) {
                        for (tracker in trackers) {
                            if (tracker.shipmentId == searchField) {
                                searchField = ""
                                return@Button
                            }
                        }
                        val tracker = TrackerViewHelper()
                        tracker.trackShipment(searchField)
                        tracker.update()
                        trackers.add(tracker)
                        searchField = ""
                    }

                }) {
                    Text("Track")
                }
            }
            Spacer(Modifier.height(16.dp))
            //organize in a column each shipment being watched
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(trackers) { tracker ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Tracking Shipment ${tracker.shipmentId}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Button(onClick = {
                                    tracker.stopTracking()
                                    trackers.remove(tracker)
                                }) {
                                    Text("Remove")
                                }
                            }
                            //This will render the specific stats for the tracker
                            TrackerView(tracker)
                        }
                    }
                    Divider()
                }
            }
        }
    }
}