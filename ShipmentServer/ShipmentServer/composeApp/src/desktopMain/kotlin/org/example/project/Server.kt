package org.example.project

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.receiveText
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.project.ShipmentHandler.receiveUpdateRequest

object Server{
    fun start(){
        embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
            .start(wait = false)
    }
}
fun Application.module() {
    routing {
        post("/update"){
            val update = call.receiveText()
            call.respond(HttpStatusCode.OK)
            receiveUpdateRequest(update)
        }
    }
}