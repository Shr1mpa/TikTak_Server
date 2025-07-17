package com.example.model.request

@kotlinx.serialization.Serializable
data class PlayerLeaveRequest(
    val name: String
)