package com.example.database.connection.impl

import com.example.database.config.AppConfig
import com.example.database.connection.ExposedConnectionFactory
import org.jetbrains.exposed.sql.Database

class PostgresExposedConnectionFactoryImpl(private val config: AppConfig) : ExposedConnectionFactory {
    override fun getDatabase(): Database {
        val db = config.databaseConfig
        return Database.connect(
            url = db.jdbcUrl,
            driver = db.driverClassName,
            user = db.user,
            password = db.password
        )
    }
}