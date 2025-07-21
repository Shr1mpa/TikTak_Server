package com.example.database.config

data class DatabaseConfig(
    val jdbcUrl: String,
    val driverClassName: String,
    val user: String,
    val password: String
)