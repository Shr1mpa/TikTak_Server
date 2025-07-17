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
    private val getGameStateUseCase: GetGameStateUseCase,
    private val getCurrentTurnUseCase: GetCurrentTurnUseCase,
    private val getGameHistoryUseCase: GetGameHistoryUseCase
) : BaseGameController() {

    suspend fun makeMove(call: ApplicationCall) {
        val sessionId = requireSessionId(call) ?: return
        val request = call.receive<MoveRequest>()
        handleResult(call) { makeMoveUseCase(sessionId, request) }
    }

    suspend fun getGameState(call: ApplicationCall) {
        val sessionId = requireSessionId(call) ?: return
        val state = getGameStateUseCase(sessionId)
        call.respond(HttpStatusCode.OK, state)
    }

    suspend fun getCurrentTurn(call: ApplicationCall) {
        val sessionId = requireSessionId(call) ?: return
        val turn = getCurrentTurnUseCase(sessionId)
        call.respond(turn)
    }

    suspend fun getHistory(call: ApplicationCall) {
        val history = getGameHistoryUseCase()
        call.respond(history)
    }
}