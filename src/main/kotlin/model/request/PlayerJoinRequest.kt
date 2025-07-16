package com.example.model.request

import kotlinx.serialization.Serializable

@Serializable
data class PlayerJoinRequest (val name: String)