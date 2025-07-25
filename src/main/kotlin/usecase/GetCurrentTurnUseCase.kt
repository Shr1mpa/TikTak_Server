package com.example.usecase

import com.example.exceptions.GameErrorType
import com.example.exceptions.ValidationResult
import com.example.manager.GameSessionManager
import com.example.model.response.CurrentTurnDto

class GetCurrentTurnUseCase(
    private val sessionManager: GameSessionManager
) {
    operator fun invoke(sessionId: String): ValidationResult<CurrentTurnDto> {
        val session = sessionManager.getSession(sessionId)
            ?: return ValidationResult.Error(
                GameErrorType.SESSION_NOT_FOUND,
                details = mapOf("sessionId" to sessionId)
            )

        sessionManager.ping(session.sessionId)
        return ValidationResult.Success(CurrentTurnDto(currentTurn = session.state.currentTurn))
    }
}