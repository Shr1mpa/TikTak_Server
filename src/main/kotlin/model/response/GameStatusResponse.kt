package com.example.model.response

import kotlinx.serialization.Serializable

@Serializable
data class GameStatusResponse(
    val board: Map<String, String?>,
    val currentTurn: String?,
    val players: Map<String, String>,
    val winner: String? = null,
    val message: String,
    val isGameOver: Boolean
)