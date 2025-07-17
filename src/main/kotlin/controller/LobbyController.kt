package com.example.controller

import com.example.manager.GameSessionManager
import com.example.model.request.PlayerJoinRequest
import com.example.model.response.CreateSessionResponse
import com.example.model.response.SessionLobbyDto
import com.example.usecase.JoinGameUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond

class LobbyController(
    private val sessionManager: GameSessionManager,
    private val joinGameUseCase: JoinGameUseCase
) : BaseGameController() {

    suspend fun createLobby(call: ApplicationCall) {
        val request = call.receive<PlayerJoinRequest>()
        val session = sessionManager.createSession(request.name)
        val response = CreateSessionResponse(
            sessionId = session.sessionId,
            message = "Сесію створено. Ви приєднались як X"
        )
        call.respond(response)
    }

    suspend fun listAvailableLobbies(call: ApplicationCall) {
        val available = sessionManager.getJoinableSessions()
        val simplified = available.map {
            SessionLobbyDto(
                sessionId = it.sessionId,
                players = it.state.players
            )
        }
        call.respond(simplified)
    }

    suspend fun getLobbyPlayers(call: ApplicationCall) {
        val sessionId = requireSessionId(call) ?: return
        val session = sessionManager.getSession(sessionId)
            ?: return call.respond(HttpStatusCode.NotFound)
        call.respond(session.state.players)
    }

    suspend fun joinLobby(call: ApplicationCall) {
        val sessionId = requireSessionId(call) ?: return
        val request = call.receive<PlayerJoinRequest>()
        handleResult(call) { joinGameUseCase(sessionId, request) }
    }
}