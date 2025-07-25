package com.example.usecase

import com.example.manager.GameSessionManager
import com.example.model.response.CreateSessionResponse
import kotlin.random.Random

class CreateLobbyUseCase(
    private val sessionManager: GameSessionManager
) {
    operator fun invoke(playerName: String): CreateSessionResponse {
        val assignedSymbol = if (Random.nextBoolean()) "X" else "O"
        val session = sessionManager.createSession(playerName, assignedSymbol)

        return CreateSessionResponse(
            sessionId = session.sessionId,
            symbol = assignedSymbol,
            message = "Session created: $assignedSymbol"
        )
    }
}