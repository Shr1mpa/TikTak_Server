package com.example.controller

import com.example.exceptions.GameErrorType
import com.example.exceptions.ValidationResult
import com.example.model.request.MoveRequest
import com.example.usecase.GetCurrentTurnUseCase
import com.example.usecase.GetGameHistoryUseCase
import com.example.usecase.GetGameStateUseCase
import com.example.usecase.MakeMoveUseCase
import com.example.utils.receiveValidatedOrError
import com.example.utils.respondIfError
import com.example.utils.unwrapOrRespond
import io.ktor.server.application.*
import io.ktor.server.response.*

class GameController(
    private val makeMoveUseCase: MakeMoveUseCase,
    private val getGameStateUseCase: GetGameStateUseCase,
    private val getCurrentTurnUseCase: GetCurrentTurnUseCase,
    private val getGameHistoryUseCase: GetGameHistoryUseCase
) : BaseGameController() {

    suspend fun makeMove(call: ApplicationCall) {
        val sessionId = call.unwrapOrRespond(call.requireSessionId()) ?: return

        val request = call.unwrapOrRespond(call.receiveValidatedOrError<MoveRequest> {
            if (player != "X" && player != "O") {
                return@receiveValidatedOrError ValidationResult.Error(
                    GameErrorType.INVALID_PLAYER,
                    mapOf("player" to player)
                )
            }

            val validCells = setOf("A1", "A2", "A3", "B1", "B2", "B3", "C1", "C2", "C3")
            if (cell !in validCells) {
                return@receiveValidatedOrError ValidationResult.Error(
                    GameErrorType.INVALID_CELL,
                    mapOf("cell" to cell)
                )
            }

            ValidationResult.Success(Unit)
        }) ?: return

        val result = makeMoveUseCase(sessionId, request)
        call.unwrapOrRespond(result)?.let {
            call.respond(it)
        }
    }

    suspend fun getGameState(call: ApplicationCall) {
        val sessionId = call.unwrapOrRespond(call.requireSessionId()) ?: return
        val result = getGameStateUseCase(sessionId)
        call.unwrapOrRespond(result)?.let {
            call.respond(it)
        }
    }

    suspend fun getCurrentTurn(call: ApplicationCall) {
        when (val sessionIdResult = call.requireSessionId()) {
            is ValidationResult.Error -> {
                call.respondIfError(sessionIdResult)
                return
            }
            is ValidationResult.Success -> {
                val sessionId = sessionIdResult.data
                when (val result = getCurrentTurnUseCase(sessionId)) {
                    is ValidationResult.Success -> call.respond(result.data)
                    is ValidationResult.Error -> call.respondIfError(result)
                }
            }
        }
    }

    suspend fun getHistory(call: ApplicationCall) {
        val history = getGameHistoryUseCase()
        call.respond(history)
    }
}