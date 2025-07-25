package com.example.usecase

import com.example.exceptions.GameErrorType
import com.example.exceptions.ValidationResult
import com.example.manager.GameSessionManager

class GetLobbyPlayersUseCase(
    private val sessionManager: GameSessionManager
) {
    operator fun invoke(sessionId: String): ValidationResult<Map<String, String>> {
        val session = sessionManager.getSession(sessionId)
            ?: return ValidationResult.Error(
                GameErrorType.SESSION_NOT_FOUND,
                details = mapOf("sessionId" to sessionId)
            )

        return ValidationResult.Success(session.state.players)
    }
}