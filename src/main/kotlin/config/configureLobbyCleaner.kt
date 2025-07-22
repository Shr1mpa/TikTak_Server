package com.example.config

import io.ktor.server.application.Application
import org.koin.ktor.ext.get
import utils.LobbyCleaner

fun Application.configureLobbyCleaner() {
    val lobbyCleaner = get<LobbyCleaner>()
    lobbyCleaner.start()
}