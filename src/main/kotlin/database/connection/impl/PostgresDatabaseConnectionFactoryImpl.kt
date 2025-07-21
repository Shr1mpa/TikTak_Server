package com.example.database.connection.impl

import com.example.database.config.AppConfig
import com.example.database.connection.DatabaseConnectionFactory
import java.sql.Connection
import java.sql.DriverManager

class PostgresDatabaseConnectionFactoryImpl(private val config: AppConfig) : DatabaseConnectionFactory {
    override fun getConnection(): Connection {
        val db = config.databaseConfig
        return DriverManager.getConnection(db.jdbcUrl, db.user, db.password)
    }
}