package com.example.usecase

import com.example.manager.GameSessionManager
import com.example.model.response.JoinResponse
import com.example.model.request.PlayerJoinRequest

class JoinGameUseCase(
    private val sessionManager: GameSessionManager
) {
    operator fun invoke(sessionId: String, request: PlayerJoinRequest): Result<JoinResponse> {
        val name = request.name.trim()

        val result = sessionManager.addPlayerToSession(sessionId, name)

        return result.map { symbol ->
            JoinResponse(
                name = name,
                symbol = symbol,
                message = "Гравець успішно приєднався як $symbol"
            )
        }
    }
}