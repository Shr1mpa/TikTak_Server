package com.example.model.response
import kotlinx.serialization.Serializable
@Serializable
data class GameResult(
    val players: Map<String, String>,
    val winner: String,
    val finalBoard: Map<String, String?>
)