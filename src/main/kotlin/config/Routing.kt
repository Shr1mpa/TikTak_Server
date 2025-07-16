package com.example.config

import com.example.controller.AuthorizeController
import com.example.controller.GameController
import com.example.routes.authorizeRoute
import com.example.routes.gameRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val authorizeController by inject<AuthorizeController>()
    val gameController by inject<GameController>()
    routing {
        authorizeRoute(authorizeController)
        gameRoutes(gameController)
    }
}
