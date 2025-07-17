package com.example.usecase

import com.example.manager.GameSessionManager
import com.example.model.WinnerResult
import com.example.model.request.MoveRequest
import com.example.model.response.GameResult
import com.example.model.response.MoveResult
import com.example.repository.GameHistoryRepository
import com.example.utils.GameRulesValidator
import com.example.utils.WinnerChecker.Companion.checkWinner
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class MakeMoveUseCase(
    private val sessionManager: GameSessionManager,
    private val historyRepository: GameHistoryRepository
) {
    operator fun invoke(sessionId: String, request: MoveRequest): Result<MoveResult> {
        val session = sessionManager.getSession(sessionId)
            ?: return Result.failure(IllegalStateException("Сесію не знайдено"))

        val state = session.state

        GameRulesValidator.validateMove(state, request)

        val updatedBoard = state.board.toMutableMap()
        updatedBoard[request.cell] = request.player

        val winnerResult = checkWinner(updatedBoard)
        val winnerName = when (winnerResult) {
            WinnerResult.X, WinnerResult.O -> state.players[winnerResult.name]
            WinnerResult.DRAW, WinnerResult.NONE -> null
        }

        session.state = state.copy(
            board = updatedBoard,
            currentTurn = if (winnerResult == WinnerResult.NONE) togglePlayer(request.player) else state.currentTurn,
            winnerResult = winnerResult
        )

        if (winnerResult != WinnerResult.NONE) {
            val result = GameResult(
                sessionId = sessionId,
                players = state.players,
                board = updatedBoard,
                winner = winnerName,
                endedAt = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            )
            historyRepository.save(result)
        }

        val message = when (winnerResult) {
            WinnerResult.X, WinnerResult.O -> "Гру виграв $winnerName"
            WinnerResult.DRAW -> "Гра завершена в нічию"
            WinnerResult.NONE -> "Хід прийнято"
        }

        return Result.success(
            MoveResult(
                board = updatedBoard,
                currentTurn = session.state.currentTurn,
                winner = winnerName,
                message = message
            )
        )
    }

    private fun togglePlayer(current: String): String = if (current == "X") "O" else "X"
}