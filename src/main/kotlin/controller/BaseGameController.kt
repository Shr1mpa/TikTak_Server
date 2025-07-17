package com.example.controller

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseGameController {
    protected suspend fun requireSessionId(call: ApplicationCall): String? {
        return call.parameters["sessionId"] ?: run {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Session ID is required"))
            null
        }
    }

    protected suspend inline fun <T> handleResult(
        call: ApplicationCall,
        crossinline block: suspend () -> Result<T>
    ) {
        val result = withContext(Dispatchers.IO) { block() }

        result.fold(
            onSuccess = { call.respond(HttpStatusCode.OK, it as Any) },
            onFailure = {
                when (it) {
                    is IllegalArgumentException -> call.respond(HttpStatusCode.BadRequest, mapOf("error" to (it.message ?: "Invalid input")))
                    else -> {
                        it.printStackTrace()
                        call.respond(HttpStatusCode.InternalServerError)
                    }
                }
            }
        )
    }
}