package com.example.controller

import com.example.model.request.MoveRequest
import com.example.usecase.GetCurrentTurnUseCase
import com.example.usecase.GetGameHistoryUseCase
import com.example.usecase.MakeMoveUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond

class GameController(
    private val makeMoveUseCase: MakeMoveUseCase,
    private val getGameHistoryUseCase: GetGameHistoryUseCase,
    private val getCurrentTurnUseCase: GetCurrentTurnUseCase
) {
    suspend fun makeMove(call: ApplicationCall) {
        val request = call.receive<MoveRequest>()

        val result = makeMoveUseCase(request)

        result
            .onSuccess { moveResult -> call.respond(HttpStatusCode.OK, moveResult) }
            .onFailure { error -> call.respond(HttpStatusCode.BadRequest, mapOf("error" to error.message)) }
    }

    suspend fun getAll(call: ApplicationCall) {
        val history = getGameHistoryUseCase()
        call.respond(history)
    }

    suspend fun getCurrentTurn(call: ApplicationCall) {
        val result = getCurrentTurnUseCase()
        call.respond(result)
    }
}