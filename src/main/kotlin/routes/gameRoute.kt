package com.example.routes

import com.example.controller.GameController
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.gameRoutes(gameController: GameController) {
    route("/game/{sessionId}") {
        post("/move") { gameController.makeMove(call) }
        get("/state") { gameController.getGameState(call) }
        get("/current-turn") { gameController.getCurrentTurn(call) }
    }

    route("/game") {
        get("/history") { gameController.getHistory(call) }
    }
}