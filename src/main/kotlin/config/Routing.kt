package com.example.config

import com.example.controller.GameController
import com.example.controller.SessionController
import com.example.routes.gameRoutes
import com.example.routes.sessionRoutes
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val sessionController by inject<SessionController>()
    val gameController by inject<GameController>()
    routing {
        sessionRoutes(sessionController)
        gameRoutes(gameController)
    }
}
