package com.example.usecase

import com.example.manager.GameSessionManager
import com.example.model.response.SessionLobbyDto

class ListAvailableLobbiesUseCase(
    private val sessionManager: GameSessionManager
) {
    operator fun invoke(): List<SessionLobbyDto> {
        return sessionManager.getJoinableSessions().map {
            SessionLobbyDto(
                sessionId = it.sessionId,
                players = it.state.players
            )
        }
    }
}