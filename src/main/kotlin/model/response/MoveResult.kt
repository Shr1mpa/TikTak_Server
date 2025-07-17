package com.example.model.response

import kotlinx.serialization.Serializable

@Serializable
data class MoveResult(
    val board: Map<String, String?>,
    val currentTurn: String?,
    val winner: String? = null,
    val message: String,
    val isGameOver: Boolean
)