package com.example.model

data class GameSession(
    val sessionId: String,
    var state: GameState
)