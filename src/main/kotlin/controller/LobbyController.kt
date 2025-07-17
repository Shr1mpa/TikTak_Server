package com.example.controller

import com.example.exceptions.ValidationException
import com.example.manager.GameSessionManager
import com.example.model.request.PlayerJoinRequest
import com.example.model.request.PlayerLeaveRequest
import com.example.model.response.CreateSessionResponse
import com.example.model.response.SessionLobbyDto
import com.example.usecase.JoinGameUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.request.ContentTransformationException
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import kotlin.random.Random

class LobbyController(
    private val sessionManager: GameSessionManager,
    private val joinGameUseCase: JoinGameUseCase
) : BaseGameController() {

    suspend fun createLobby(call: ApplicationCall) = withErrorHandling(call) {
        val request = call.receiveValidated<PlayerJoinRequest> {
            if (name.isBlank()) throw ValidationException("Імʼя не може бути порожнім")
        }

        val assignedSymbol = if (Random.nextBoolean()) "X" else "O"
        val session = sessionManager.createSession(request.name, assignedSymbol)

        call.respond(
            CreateSessionResponse(
                sessionId = session.sessionId,
                symbol = assignedSymbol,
                message = "Сесія створена, ваш символ: $assignedSymbol"
            )
        )
    }

    suspend fun listAvailableLobbies(call: ApplicationCall) = withErrorHandling(call) {
        val availableSessions = sessionManager.getJoinableSessions()
        val response = availableSessions.map {
            SessionLobbyDto(
                sessionId = it.sessionId,
                players = it.state.players
            )
        }
        call.respond(response)
    }

    suspend fun getLobbyPlayers(call: ApplicationCall) = withErrorHandling(call) {
        val sessionId = call.requireSessionId()
        val session = sessionManager.getSession(sessionId)
            ?: throw NotFoundException("Сесія не знайдена")

        call.respond(session.state.players)
    }

    suspend fun joinLobby(call: ApplicationCall) = withErrorHandling(call) {
        val sessionId = call.requireSessionId()
        val request = call.receiveValidated<PlayerJoinRequest> {
            if (name.isBlank()) throw ValidationException("Імʼя не може бути порожнім")
        }

        joinGameUseCase(sessionId, request).fold(
            onSuccess = { call.respond(it) },
            onFailure = { throw it }
        )
    }

    suspend fun leaveLobby(call: ApplicationCall) = withErrorHandling(call) {
        val sessionId = call.requireSessionId()
        val request = call.receive<PlayerLeaveRequest>()

        sessionManager.removePlayerFromSession(sessionId, request.name)
            .onFailure { throw it }

        call.respond(mapOf("message" to "Гравець ${request.name} покинув лоббі"))
    }
}