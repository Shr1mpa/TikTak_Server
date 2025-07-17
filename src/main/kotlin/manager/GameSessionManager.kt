package com.example.manager

import com.example.model.GameSession
import com.example.model.GameState
import com.example.utils.initEmptyBoard
import java.util.UUID

class GameSessionManager {
    private val sessions = mutableMapOf<String, GameSession>()

    fun createSession(initialPlayerName: String): GameSession {
        val id = UUID.randomUUID().toString()
        val initialState = GameState(
            board = initEmptyBoard(),
            currentTurn = "X",
            players = mapOf("X" to initialPlayerName)
        )
        val session = GameSession(id, initialState)
        sessions[id] = session
        return session
    }

    fun getSession(id: String): GameSession? = sessions[id]

    fun resetSession(id: String) {
        sessions[id]?.state = GameState(board = initEmptyBoard(), currentTurn = "X", players = emptyMap())
    }

    fun addPlayerToSession(id: String, name: String): Result<String> {
        val session = sessions[id] ?: return Result.failure(Exception("Session not found"))
        val players = session.state.players.toMutableMap()

        if (players.containsValue(name)) {
            return Result.success(players.entries.first { it.value == name }.key) // Return existing symbol
        }

        val symbol = when {
            !players.containsKey("X") -> "X"
            !players.containsKey("O") -> "O"
            else -> return Result.failure(Exception("Session full"))
        }

        players[symbol] = name
        session.state = session.state.copy(players = players)
        return Result.success(symbol)
    }

    fun getAllSessions(): List<GameSession> = sessions.values.toList()
    fun getJoinableSessions(): List<GameSession> {
        return sessions.values.filter { it.state.players.size < 2 }
    }
}