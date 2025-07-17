package com.example.model.response

import kotlinx.serialization.Serializable

@Serializable
data class SessionLobbyDto(
    val sessionId: String,
    val players: Map<String, String>
)