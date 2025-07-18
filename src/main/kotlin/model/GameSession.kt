package com.example.model

import kotlinx.coroutines.sync.Mutex

data class GameSession(
    val sessionId: String,
    var state: GameState,
    val mutex: Mutex = Mutex()
)