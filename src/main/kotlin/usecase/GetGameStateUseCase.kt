package com.example.usecase

import com.example.exceptions.GameErrorType
import com.example.exceptions.ValidationResult
import com.example.manager.GameSessionManager
import com.example.model.GameMessage
import com.example.model.WinnerResult
import com.example.model.response.GameStatusResponse


class GetGameStateUseCase(
    private val sessionManager: GameSessionManager
) {
    operator fun invoke(sessionId: String): ValidationResult<GameStatusResponse> {
        val session = sessionManager.getSession(sessionId)
            ?: return ValidationResult.Error(
                type = GameErrorType.SESSION_NOT_FOUND,
                details = mapOf("sessionId" to sessionId)
            )

        sessionManager.ping(session.sessionId)
        val state = session.state
        val winnerResult = state.winnerResult

        val (messageCode, messageParams) = when {
            winnerResult == WinnerResult.DRAW ->
                GameMessage.DRAW to emptyMap()

            winnerResult != WinnerResult.NONE -> {
                val winnerName = state.players[winnerResult.name] ?: winnerResult.name
                GameMessage.WINNER to mapOf("player" to winnerName)
            }

            else ->
                GameMessage.PLAYER_TURN to mapOf("player" to state.currentTurn)
        }

        return ValidationResult.Success(
            GameStatusResponse(
                board = state.board,
                currentTurn = if (winnerResult == WinnerResult.NONE) state.currentTurn else null,
                players = state.players,
                winner = when (winnerResult) {
                    WinnerResult.X, WinnerResult.O -> state.players[winnerResult.name]
                    else -> null
                },
                messageCode = messageCode,
                messageParams = messageParams,
                isGameOver = winnerResult != WinnerResult.NONE
            )
        )
    }
}