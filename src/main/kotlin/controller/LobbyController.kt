package com.example.controller

import com.example.exceptions.GameErrorType
import com.example.exceptions.ValidationResult
import com.example.model.request.PlayerJoinRequest
import com.example.model.request.PlayerLeaveRequest
import com.example.usecase.CreateLobbyUseCase
import com.example.usecase.GetLobbyPlayersUseCase
import com.example.usecase.JoinGameUseCase
import com.example.usecase.ListAvailableLobbiesUseCase
import com.example.utils.receiveValidatedOrError
import com.example.utils.unwrapOrRespond
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import usecase.LeaveLobbyUseCase
import kotlin.random.Random

class LobbyController(
    private val createLobbyUseCase: CreateLobbyUseCase,
    private val joinGameUseCase: JoinGameUseCase,
    private val listAvailableLobbiesUseCase: ListAvailableLobbiesUseCase,
    private val getLobbyPlayersUseCase: GetLobbyPlayersUseCase,
    private val leaveLobbyUseCase: LeaveLobbyUseCase,
) : BaseGameController() {

    suspend fun createLobby(call: ApplicationCall) {
        val request = call.unwrapOrRespond(call.receiveValidatedOrError<PlayerJoinRequest> {
            if (name.isBlank()) {
                return@receiveValidatedOrError ValidationResult.Error(GameErrorType.EMPTY_NAME)
            }
            ValidationResult.Success(Unit)
        }) ?: return

        val response = createLobbyUseCase(request.name)
        call.respond(response)
    }

    suspend fun listAvailableLobbies(call: ApplicationCall) {
        val response = listAvailableLobbiesUseCase()
        call.respond(response)
    }

    suspend fun getLobbyPlayers(call: ApplicationCall) {
        val sessionId = call.unwrapOrRespond(call.requireSessionId()) ?: return
        val result = getLobbyPlayersUseCase(sessionId)
        call.unwrapOrRespond(result)?.let {
            call.respond(it)
        }
    }

    suspend fun joinLobby(call: ApplicationCall) {
        val sessionId = call.unwrapOrRespond(call.requireSessionId()) ?: return

        val request = call.unwrapOrRespond(call.receiveValidatedOrError<PlayerJoinRequest> {
            if (name.isBlank()) {
                return@receiveValidatedOrError ValidationResult.Error(GameErrorType.EMPTY_NAME)
            }
            ValidationResult.Success(Unit)
        }) ?: return

        call.unwrapOrRespond(joinGameUseCase(sessionId, request))?.let {
            call.respond(it)
        }
    }

    suspend fun leaveLobby(call: ApplicationCall) {
        val sessionId = call.unwrapOrRespond(call.requireSessionId()) ?: return

        val request = call.unwrapOrRespond(call.receiveValidatedOrError<PlayerLeaveRequest> {
            if (name.isBlank()) {
                return@receiveValidatedOrError ValidationResult.Error(GameErrorType.EMPTY_NAME)
            }
            ValidationResult.Success(Unit)
        }) ?: return

        val result = leaveLobbyUseCase(sessionId, request.name)
        call.unwrapOrRespond(result)?.let {
            call.respond(it)
        }
    }
}