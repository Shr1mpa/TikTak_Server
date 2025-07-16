package com.example.config

import com.example.exceptions.GameException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureExceptionHandling() {
    install(StatusPages) {
        exception<GameException> { call, cause ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = mapOf("error" to cause.message)
            )
        }

        exception<Throwable> { call, cause ->
            cause.printStackTrace()
            call.respond(
                status = HttpStatusCode.InternalServerError,
                message = mapOf("error" to "Внутрішня помилка сервера")
            )
        }
    }
}