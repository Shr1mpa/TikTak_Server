package com.example.config

import com.example.database.config.AppConfigLoader
import com.example.database.connection.ExposedDatabaseFactory
import com.example.database.connection.impl.PostgresExposedConnectionFactoryImpl
import io.ktor.server.application.*

fun Application.configureDatabase() {
    val config = AppConfigLoader.load()
    val factory = PostgresExposedConnectionFactoryImpl(config)
    ExposedDatabaseFactory.init(factory)
}