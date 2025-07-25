package com.example.manager

import com.example.exceptions.JoinPlayerResult
import com.example.exceptions.LeavePlayerResult
import com.example.model.GameSession
import com.example.model.GameState
import com.example.model.WinnerResult
import com.example.utils.initEmptyBoard
import java.util.*

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

    fun addPlayerToSession(id: String, name: String): JoinPlayerResult {
        val session = sessions[id] ?: return JoinPlayerResult.SessionNotFound
        val players = session.state.players.toMutableMap()

        if (players.containsValue(name)) {
            val existingSymbol = players.entries.first { it.value == name }.key
            return JoinPlayerResult.Success(existingSymbol)
        }

        val availableSymbol = when {
            !players.containsKey("X") -> "X"
            !players.containsKey("O") -> "O"
            else -> return JoinPlayerResult.SessionFull
        }

        players[availableSymbol] = name
        session.state = session.state.copy(players = players)
        return JoinPlayerResult.Success(availableSymbol)
    }

    fun isJoinable(session: GameSession): Boolean {
        val state = session.state
        return state.players.size < 2 && state.winnerResult == WinnerResult.NONE
    }

    fun getJoinableSessions(): List<GameSession> {
        return sessions.values.filter(::isJoinable)
    }

    fun removePlayerFromSession(sessionId: String, playerName: String): LeavePlayerResult {
        val session = sessions[sessionId] ?: return LeavePlayerResult.SessionNotFound

        val updatedPlayers = session.state.players.filterValues { it != playerName }

        if (updatedPlayers.isEmpty()) {
            sessions.remove(sessionId)
        } else {
            session.state = session.state.copy(players = updatedPlayers)
        }

        return LeavePlayerResult.Success
    }

    fun ping(sessionId: String) {
        sessions[sessionId]?.lastPing = System.currentTimeMillis()
    }

    fun removeInactiveLobbies(ttl: Long) {
        val now = System.currentTimeMillis()
        sessions.entries.removeIf { (_, lobby) ->
            now - lobby.lastPing > ttl
        }
    }
}