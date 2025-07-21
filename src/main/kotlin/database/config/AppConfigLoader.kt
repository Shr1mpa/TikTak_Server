package com.example.database.config

object AppConfigLoader {
    fun load(): AppConfig {
        val jdbcUrl = System.getenv("JDBC_URL") ?: error("JDBC_URL not set")
        val driver = System.getenv("DB_DRIVER") ?: error("DB_DRIVER not set")
        val user = System.getenv("DB_USER") ?: error("DB_USER not set")
        val password = System.getenv("DB_PASSWORD") ?: error("DB_PASSWORD not set")

        return AppConfig(
            databaseConfig = DatabaseConfig(
                jdbcUrl = jdbcUrl,
                driverClassName = driver,
                user = user,
                password = password
            )
        )
    }
}