package com.example.config

import io.ktor.server.application.*
import com.example.database.config.AppConfigLoader
import com.example.database.connection.ExposedDatabaseFactory
import com.example.database.connection.impl.PostgresExposedConnectionFactoryImpl

fun Application.configureDatabase() {
    val config = AppConfigLoader.load()
    val factory = PostgresExposedConnectionFactoryImpl(config)
    ExposedDatabaseFactory.init(factory)
}