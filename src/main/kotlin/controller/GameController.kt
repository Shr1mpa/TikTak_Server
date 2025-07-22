package com.example.controller

import com.example.exceptions.ValidationException
import com.example.model.request.MoveRequest
import com.example.usecase.GetCurrentTurnUseCase
import com.example.usecase.GetGameHistoryUseCase
import com.example.usecase.GetGameStateUseCase
import com.example.usecase.MakeMoveUseCase
import io.ktor.server.application.*
import io.ktor.server.response.*

class GameController(
    private val makeMoveUseCase: MakeMoveUseCase,
    private val getGameStateUseCase: GetGameStateUseCase,
    private val getCurrentTurnUseCase: GetCurrentTurnUseCase,
    private val getGameHistoryUseCase: GetGameHistoryUseCase
) : BaseGameController() {

    suspend fun makeMove(call: ApplicationCall) {
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

        val result = makeMoveUseCase(sessionId, request)
        call.respond(result.getOrThrow())
    }

    suspend fun getGameState(call: ApplicationCall) {
        val sessionId = call.requireSessionId()
        val state = getGameStateUseCase(sessionId)
        call.respond(state)
    }

    suspend fun getCurrentTurn(call: ApplicationCall) {
        val sessionId = call.requireSessionId()
        val turn = getCurrentTurnUseCase(sessionId)
        call.respond(turn)
    }

    suspend fun getHistory(call: ApplicationCall) {
        val history = getGameHistoryUseCase()
        call.respond(history)
    }
}