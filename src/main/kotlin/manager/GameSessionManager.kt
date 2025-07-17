package com.example.manager

import com.example.model.GameSession
import com.example.model.GameState
import com.example.utils.initEmptyBoard
import java.util.UUID

class GameSessionManager {
    private val sessions = mutableMapOf<String, GameSession>()

    fun createSession(initialPlayerName: String, symbol: String): GameSession {
        val id = UUID.randomUUID().toString()
        val initialState = GameState(
            board = initEmptyBoard(),
            currentTurn = "X",
            players = mapOf(symbol to initialPlayerName)
        )
        val session = GameSession(id, initialState)
        sessions[id] = session
        return session
    }

    fun getSession(id: String): GameSession? = sessions[id]

    fun resetSession(id: String) {
        sessions[id]?.state = GameState(
            board = initEmptyBoard(),
            currentTurn = "X",
            players = emptyMap()
        )
    }

    fun addPlayerToSession(id: String, name: String): Result<String> {
        val session = sessions[id] ?: return Result.failure(Exception("Session not found"))
        val players = session.state.players.toMutableMap()

        if (players.containsValue(name)) {
            return Result.success(players.entries.first { it.value == name }.key)
        }

        val availableSymbol = when {
            !players.containsKey("X") -> "X"
            !players.containsKey("O") -> "O"
            else -> return Result.failure(Exception("Session full"))
        }

        players[availableSymbol] = name
        session.state = session.state.copy(players = players)
        return Result.success(availableSymbol)
    }

    fun getJoinableSessions(): List<GameSession> {
        return sessions.values.filter { it.state.players.size < 2 }
    }

    fun removePlayerFromSession(sessionId: String, playerName: String): Result<Unit> {
        val session = sessions[sessionId] ?: return Result.failure(Exception("Сесію не знайдено"))

        val updatedPlayers = session.state.players.filterValues { it != playerName }

        if (updatedPlayers.isEmpty()) {
            sessions.remove(sessionId)
        } else {
            session.state = session.state.copy(players = updatedPlayers)
        }

        return Result.success(Unit)
    }
}