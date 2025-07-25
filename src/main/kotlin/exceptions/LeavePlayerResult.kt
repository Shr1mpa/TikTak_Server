package com.example.exceptions

sealed class LeavePlayerResult {
    object Success : LeavePlayerResult()
    object SessionNotFound : LeavePlayerResult()
}