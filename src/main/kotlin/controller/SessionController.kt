package com.example.controller

import com.example.manager.GameSessionManager
import com.example.model.request.PlayerJoinRequest
import com.example.model.response.CreateSessionResponse
import com.example.model.response.SessionLobbyDto
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond

class SessionController(
    private val sessionManager: GameSessionManager
) {
    suspend fun create(call: ApplicationCall) {
        val request = call.receive<PlayerJoinRequest>()
        val session = sessionManager.createSession(request.name)

        val response = CreateSessionResponse(
            sessionId = session.sessionId,
            message = "Сесію створено. Ви приєднались як X"
        )

        call.respond(response)
    }

    suspend fun state(call: ApplicationCall) {
        val sessionId = call.parameters["sessionId"]
            ?: return call.respond(HttpStatusCode.BadRequest)

        val session = sessionManager.getSession(sessionId)
            ?: return call.respond(HttpStatusCode.NotFound)

        call.respond(session.state)
    }

    suspend fun listAvailableSessions(call: ApplicationCall) {
        val available = sessionManager.getJoinableSessions()

        val simplified = available.map {
            SessionLobbyDto(
                sessionId = it.sessionId,
                players = it.state.players
            )
        }
        call.respond(simplified)
    }

    suspend fun getPlayers(call: ApplicationCall) {
        val sessionId = call.parameters["sessionId"]
            ?: return call.respond(HttpStatusCode.BadRequest)

        val session = sessionManager.getSession(sessionId)
            ?: return call.respond(HttpStatusCode.NotFound)

        call.respond(session.state.players)
    }
}