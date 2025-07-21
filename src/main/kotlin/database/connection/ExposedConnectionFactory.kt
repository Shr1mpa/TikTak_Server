package com.example.database.connection

import org.jetbrains.exposed.sql.Database

interface ExposedConnectionFactory {
    fun getDatabase(): Database
}