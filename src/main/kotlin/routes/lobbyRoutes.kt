package com.example.routes

import com.example.controller.LobbyController
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route


fun Route.lobbyRoutes(lobbyController: LobbyController) {
    route("/lobby") {
        post("/create") { lobbyController.createLobby(call) }
        get("/available") { lobbyController.listAvailableLobbies(call) }
        get("/{sessionId}/players") { lobbyController.getLobbyPlayers(call) }
        post("/{sessionId}/join") { lobbyController.joinLobby(call) }
        post("/{sessionId}/leave") { lobbyController.leaveLobby(call) }
    }
}