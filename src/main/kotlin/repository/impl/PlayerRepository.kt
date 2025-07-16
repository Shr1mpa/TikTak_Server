package com.example.repository.impl

class PlayerRepository {
    private val players = mutableMapOf<String, String>()

    fun findSymbolByName(name: String): String? {
        return players.entries.find { it.value == name }?.key
    }

    fun isAvailable(symbol: String): Boolean = symbol !in players

    fun assignPlayer(symbol: String, name: String) {
        players[symbol] = name
    }

    fun getAll(): Map<String, String> = players.toMap()

    fun isFull(): Boolean = players.size >= 2

    fun reset() = players.clear()
}