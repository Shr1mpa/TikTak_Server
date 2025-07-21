package com.example.model.response
import kotlinx.serialization.Serializable
@Serializable
data class GameResult(
    val sessionId: String,
    val players: Map<String, String?>,
    val board: Map<String, String?>,
    val winner: String? = null,
    val endedAt: String
)