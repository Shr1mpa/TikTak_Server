package com.example.controller

import com.example.model.response.ErrorResponse
import com.example.model.request.PlayerJoinRequest
import com.example.usecase.JoinGameUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond

class AuthorizeController(
    private val joinGameUseCase: JoinGameUseCase,
) {
    suspend fun join(call: ApplicationCall) {
        val request = call.receive<PlayerJoinRequest>()
        val result = joinGameUseCase(request)

        result.onSuccess {
            call.respond(it)
        }.onFailure {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(it.message ?: "Unknown error"))
        }
    }
}