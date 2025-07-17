package com.example.controller

import com.example.model.request.MoveRequest
import com.example.model.request.PlayerJoinRequest
import com.example.usecase.GetCurrentTurnUseCase
import com.example.usecase.GetGameHistoryUseCase
import com.example.usecase.GetGameStateUseCase
import com.example.usecase.JoinGameUseCase
import com.example.usecase.MakeMoveUseCase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GameController(
    private val makeMoveUseCase: MakeMoveUseCase,
    private val getGameHistoryUseCase: GetGameHistoryUseCase,
    private val getCurrentTurnUseCase: GetCurrentTurnUseCase,
    private val getGameStateUseCase: GetGameStateUseCase,
    private val joinGameUseCase: JoinGameUseCase
) {
    suspend fun makeMove(call: ApplicationCall) {
        val sessionId = call.parameters["sessionId"]
            ?: return call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Session ID is missing"))

        val request = call.receive<MoveRequest>()

        val result = withContext(Dispatchers.Default) {
            makeMoveUseCase(sessionId, request)
        }

        result
            .onSuccess { moveResult -> call.respond(HttpStatusCode.OK, moveResult) }
            .onFailure { error -> call.respond(HttpStatusCode.BadRequest, mapOf("error" to error.message)) }
    }

    suspend fun join(call: ApplicationCall) {
        val sessionId = call.parameters["sessionId"]
            ?: return call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Session ID is missing"))

        val request = call.receive<PlayerJoinRequest>()
        val result = withContext(Dispatchers.Default) {
            joinGameUseCase(sessionId, request)
        }

        result.onSuccess {
            call.respond(it)
        }.onFailure {
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to it.message))
        }
    }

    suspend fun getAll(call: ApplicationCall) {
        val history = getGameHistoryUseCase()
        call.respond(history)
    }

    suspend fun getCurrentTurn(call: ApplicationCall) {
        val sessionId = call.parameters["sessionId"]
            ?: return call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Session ID is missing"))

        val result = getCurrentTurnUseCase(sessionId)
        call.respond(result)
    }

    suspend fun getState(call: ApplicationCall) {
        val sessionId = call.parameters["sessionId"]
            ?: return call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Session ID is missing"))

        val result = getGameStateUseCase(sessionId)
        call.respond(HttpStatusCode.OK, result)
    }


}