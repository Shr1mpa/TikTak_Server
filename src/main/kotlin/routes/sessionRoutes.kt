package com.example.routes

import com.example.controller.SessionController
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route


fun Route.sessionRoutes(sessionController: SessionController) {
    route("/session") {
        post("/create") {
            sessionController.create(call)
        }
        get("/available") {
            sessionController.listAvailableSessions(call)
        }
    }
}