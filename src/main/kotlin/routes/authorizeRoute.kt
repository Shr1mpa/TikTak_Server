package com.example.routes

import com.example.controller.AuthorizeController
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route


fun Route.authorizeRoute(authorizeController: AuthorizeController) {
    route("/authorize") {
        post("/join") {
            authorizeController.join(call)
        }
    }
}