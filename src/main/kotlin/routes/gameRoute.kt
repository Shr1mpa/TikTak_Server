package com.example.routes

import com.example.controller.GameController
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.gameRoutes(gameController: GameController) {
    route("/session/{sessionId}") {
        post("/join") {
            gameController.join(call)
        }
        post("/move") {
            gameController.makeMove(call)
        }
        get("/state") {
            gameController.getState(call)
        }
        get("/current-turn") {
            gameController.getCurrentTurn(call)
        }
    }

    route("/game") {
        get("/history") {
            gameController.getAll(call)
        }
    }
}