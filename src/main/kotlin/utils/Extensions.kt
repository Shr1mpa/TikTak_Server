package com.example.utils

import com.example.exceptions.GameErrorType
import com.example.exceptions.ValidationResult
import com.example.model.response.ErrorResponse
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import org.slf4j.LoggerFactory

suspend fun ApplicationCall.respondIfError(result: ValidationResult<*>): Boolean {
    if (result !is ValidationResult.Error) return false

    val logger = LoggerFactory.getLogger("Validation")

    logger.warn("Validation error: ${result.type} — details: ${result.details}")

    val response = ErrorResponse(
        error = result.type.name,
        code = result.type.code,
        details = result.details?.mapValues { it.value}
    )

    respond(status = result.type.statusCode, message = response)
    return true
}

suspend inline fun <reified T : Any> ApplicationCall.receiveValidatedOrError(
    crossinline validate: T.() -> ValidationResult<*> = { ValidationResult.Success(Unit) }
): ValidationResult<T> {
    return try {
        val body = receive<T>()
        when (val result = body.validate()) {
            is ValidationResult.Success -> ValidationResult.Success(body)
            is ValidationResult.Error -> result
        }
    } catch (e: Exception) {
        ValidationResult.Error(
            GameErrorType.INVALID_BODY,
            details = mapOf("cause" to (e.message ?: "unknown"))
        )
    }
}

suspend fun <T> ApplicationCall.unwrapOrRespond(
    result: ValidationResult<T>
): T? {
    return when (result) {
        is ValidationResult.Success -> result.data
        is ValidationResult.Error -> {
            respondIfError(result)
            null
        }
    }
}