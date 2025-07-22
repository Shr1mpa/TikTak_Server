package com.example.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.head
import io.ktor.server.routing.route

fun Route.healthPath() {
    route("/health") {
        get {
            call.respondText("OK")
        }
        head {
            call.respond(HttpStatusCode.OK)
        }
    }
}