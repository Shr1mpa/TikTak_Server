package com.example.database.connection

import java.sql.Connection

interface DatabaseConnectionFactory {
    fun getConnection(): Connection
}