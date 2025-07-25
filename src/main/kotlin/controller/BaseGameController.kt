package com.example.controller

import com.example.exceptions.GameErrorType
import com.example.exceptions.ValidationResult
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

    protected suspend fun ApplicationCall.requireSessionId(): ValidationResult<String> {
        val id = parameters["sessionId"]
            ?: return ValidationResult.Error(
                type = GameErrorType.MISSING_SESSION_ID,
                details = mapOf("param" to "sessionId")
            )

        return ValidationResult.Success(id)
    }
}
