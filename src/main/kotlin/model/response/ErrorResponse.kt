package com.example.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val error: String,
    val code: Int,
    val details: Map<String, String>? = null
)