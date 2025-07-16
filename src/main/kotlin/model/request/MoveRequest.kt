package com.example.model.request

import kotlinx.serialization.Serializable

@Serializable
data class MoveRequest (
    val player: String,
    val cell: String
)