package com.example.controller

import com.example.model.request.MoveRequest
import com.example.usecase.GetCurrentTurnUseCase
import com.example.usecase.GetGameHistoryUseCase
import com.example.usecase.GetGameStateUseCase
import com.example.usecase.MakeMoveUseCase
import io.ktor.server.application.ApplicationCall
import com.example.exceptions.ValidationException
import io.ktor.server.response.respond

class GameController(
    private val makeMoveUseCase: MakeMoveUseCase,
    private val getGameStateUseCase: GetGameStateUseCase,
    private val getCurrentTurnUseCase: GetCurrentTurnUseCase,
    private val getGameHistoryUseCase: GetGameHistoryUseCase
) : BaseGameController() {

    suspend fun makeMove(call: ApplicationCall) = withErrorHandling(call) {
        val sessionId = call.requireSessionId()
        val request = call.receiveValidated<MoveRequest> {
            if (player != "X" && player != "O") {
                throw ValidationException("Гравець повинен бути або 'X', або 'O'")
            }

            val validCells = setOf(
                "A1", "A2", "A3",
                "B1", "B2", "B3",
                "C1", "C2", "C3"
            )

            if (cell !in validCells) {
                throw ValidationException("Невірна клітинка: $cell. Допустимі значення: ${validCells.joinToString()}")
            }
        }

        makeMoveUseCase(sessionId, request).fold(
            onSuccess = { call.respond(it) },
            onFailure = { throw it }
        )
    }

    suspend fun getGameState(call: ApplicationCall) = withErrorHandling(call) {
        val sessionId = call.requireSessionId()
        val state = getGameStateUseCase(sessionId)
        call.respond(state)
    }

    suspend fun getCurrentTurn(call: ApplicationCall) = withErrorHandling(call) {
        val sessionId = call.requireSessionId()
        val turn = getCurrentTurnUseCase(sessionId)
        call.respond(turn)
    }

    suspend fun getHistory(call: ApplicationCall) = withErrorHandling(call) {
        val history = getGameHistoryUseCase()
        call.respond(history)
    }
}