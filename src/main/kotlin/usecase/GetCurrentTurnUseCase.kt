package com.example.usecase

import com.example.manager.GameSessionManager
import com.example.model.response.CurrentTurnDto

class GetCurrentTurnUseCase(
    private val sessionManager: GameSessionManager
) {
    operator fun invoke(sessionId: String): CurrentTurnDto {
        val session = sessionManager.getSession(sessionId)
            ?: throw IllegalArgumentException("Сесію не знайдено")

        sessionManager.ping(session.sessionId)

        return CurrentTurnDto(currentTurn = session.state.currentTurn)
    }
}