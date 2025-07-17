package com.example.model.response

import kotlinx.serialization.Serializable

@Serializable
data class JoinResponse(
    val name: String,
    val symbol: String,
    val message: String
)

