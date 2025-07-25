package com.example.usecase

import com.example.exceptions.GameErrorType
import com.example.exceptions.JoinPlayerResult
import com.example.exceptions.ValidationResult
import com.example.manager.GameSessionManager
import com.example.model.request.PlayerJoinRequest
import com.example.model.response.JoinResponse

class JoinGameUseCase(
    private val sessionManager: GameSessionManager
) {
    operator fun invoke(sessionId: String, request: PlayerJoinRequest): ValidationResult<JoinResponse> {
        return when (val result = sessionManager.addPlayerToSession(sessionId, request.name.trim())) {
            is JoinPlayerResult.Success -> ValidationResult.Success(
                JoinResponse(
                    name = request.name.trim(),
                    symbol = result.symbol,
                    message = "Player connected as: ${result.symbol}"
                )
            )

            JoinPlayerResult.SessionNotFound -> ValidationResult.Error(
                GameErrorType.SESSION_NOT_FOUND,
                details = mapOf("sessionId" to sessionId)
            )

            JoinPlayerResult.SessionFull -> ValidationResult.Error(
                GameErrorType.SESSION_FULL,
                details = mapOf("sessionId" to sessionId)
            )
        }
    }
}