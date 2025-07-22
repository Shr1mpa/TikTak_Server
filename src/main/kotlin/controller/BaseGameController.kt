package com.example.controller

import com.example.exceptions.ValidationException
import io.ktor.server.application.*
import io.ktor.server.request.*

abstract class BaseGameController {
    protected suspend inline fun <reified T : Any> ApplicationCall.receiveValidated(
        validation: T.() -> Unit
    ): T {
        val request = receive<T>()
        request.validation()
        return request
    }

    protected suspend fun ApplicationCall.requireSessionId(): String {
        return parameters["sessionId"] ?: throw ValidationException("Потрібен ID сесії")
    }

}
