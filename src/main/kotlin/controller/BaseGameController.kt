package com.example.controller

import com.example.exceptions.ErrorResponse
import com.example.exceptions.ValidationException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import com.example.exceptions.NotFoundException
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

    protected suspend inline fun <T> withErrorHandling(
        call: ApplicationCall,
        crossinline block: suspend () -> T
    ) {
        try {
            block()
        } catch (e: ContentTransformationException) {
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse("Невірний формат запиту. Очікується JSON з полем 'name'")
            )
        } catch (e: ValidationException) {
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(e.message ?: "Невірні вхідні дані")
            )
        } catch (e: NotFoundException) {
            call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse(e.message ?: "Ресурс не знайдено")
            )
        } catch (e: Exception) {
            e.printStackTrace()
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse("Внутрішня помилка сервера")
            )
        }
    }
}
