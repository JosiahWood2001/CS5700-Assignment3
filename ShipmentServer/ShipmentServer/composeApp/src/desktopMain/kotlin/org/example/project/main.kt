package org.example.project

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Server.start()
    Window(
        onCloseRequest = ::exitApplication,
        title = "ShipmentTrackingSimulator",
    ) {
        App()
    }
}