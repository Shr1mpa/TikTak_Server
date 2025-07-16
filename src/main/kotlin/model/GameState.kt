package com.example.model

import kotlinx.serialization.Serializable

@Serializable
data class GameState(
    val board: Map<String, String?>,
    val currentTurn: String,
    val players: Map<String, String>,
    val winnerResult: WinnerResult = WinnerResult.NONE
)