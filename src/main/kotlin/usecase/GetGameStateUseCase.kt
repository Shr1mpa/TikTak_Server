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

        val state = session.state
        val message = when {
            state.winnerResult == WinnerResult.DRAW -> "Гра завершена в нічию"
            state.winnerResult != WinnerResult.NONE -> "Гру виграв ${state.players[state.winnerResult.name]}"
            else -> "Очікується хід гравця ${state.currentTurn}"
        }

        return GameStatusResponse(
            board = state.board,
            currentTurn = state.currentTurn,
            players = state.players,
            winner = when (state.winnerResult) {
                WinnerResult.X, WinnerResult.O -> state.players[state.winnerResult.name]
                else -> null
            },
            message = message
        )
    }
}