package com.example

import com.example.config.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureExceptionHandling()
    configureMonitoring()
    configureKoin()
    configureRouting()
    configureDatabase()
    configureLobbyCleaner()
}
