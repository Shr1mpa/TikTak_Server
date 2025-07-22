package com.example.usecase

import com.example.manager.GameSessionManager
import com.example.model.WinnerResult
import com.example.model.response.GameStatusResponse


class GetGameStateUseCase(
    private val sessionManager: GameSessionManager
) {
    operator fun invoke(sessionId: String): GameStatusResponse {
        val session = sessionManager.getSession(sessionId)
            ?: throw IllegalArgumentException("Сесію не знайдено")
        sessionManager.ping(session.sessionId)
        val state = session.state
        val winnerResult = state.winnerResult

        val message = when {
            winnerResult == WinnerResult.DRAW -> "Гра завершена в нічию"
            winnerResult != WinnerResult.NONE -> "Гру виграв ${state.players[winnerResult.name]}"
            else -> "Очікується хід гравця ${state.currentTurn}"
        }

        return GameStatusResponse(
            board = state.board,
            currentTurn = if (winnerResult == WinnerResult.NONE) state.currentTurn else null,
            players = state.players,
            winner = when (winnerResult) {
                WinnerResult.X, WinnerResult.O -> state.players[winnerResult.name]
                else -> null
            },
            message = message,
            isGameOver = winnerResult != WinnerResult.NONE
        )
    }
}