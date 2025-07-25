package com.example.model.response

import com.example.model.GameMessage
import kotlinx.serialization.Serializable

@Serializable
data class GameStatusResponse(
    val board: Map<String, String?>,
    val currentTurn: String?,
    val players: Map<String, String>,
    val winner: String?,
    val messageCode: GameMessage,
    val messageParams: Map<String, String>,
    val isGameOver: Boolean
)