package com.example.model.response

@kotlinx.serialization.Serializable
data class CreateSessionResponse(
    val sessionId: String,
    val symbol: String = "X",
    val message: String
)