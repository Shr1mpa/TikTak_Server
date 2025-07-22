package com.example.config

import com.example.controller.GameController
import com.example.controller.LobbyController
import com.example.routes.gameRoutes
import com.example.routes.healthPath
import com.example.routes.lobbyRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val lobbyController by inject<LobbyController>()
    val gameController by inject<GameController>()
    routing {
        lobbyRoutes(lobbyController)
        gameRoutes(gameController)
        healthPath()
    }
}
