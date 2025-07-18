package com.example.controller

import com.example.exceptions.GameException
import com.example.exceptions.ValidationException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import com.example.exceptions.NotFoundException
import com.example.model.response.ErrorResponse
import io.ktor.server.request.ContentTransformationException
import io.ktor.server.request.receive
import io.ktor.server.response.respond

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
