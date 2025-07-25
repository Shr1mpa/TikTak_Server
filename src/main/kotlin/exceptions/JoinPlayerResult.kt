package com.example.exceptions

sealed class JoinPlayerResult {
    data class Success(val symbol: String) : JoinPlayerResult()
    object SessionNotFound : JoinPlayerResult()
    object SessionFull : JoinPlayerResult()
}