package com.example.exceptions

sealed class ValidationResult<out T> {
    data class Success<T>(val data: T) : ValidationResult<T>()
    data class Error(val type: GameErrorType, val details: Map<String, String>? = null) : ValidationResult<Nothing>()
}