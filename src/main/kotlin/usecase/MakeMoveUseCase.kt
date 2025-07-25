package com.example.usecase

import com.example.exceptions.GameErrorType
import com.example.exceptions.ValidationResult
import com.example.manager.GameSessionManager
import com.example.model.WinnerResult
import com.example.model.request.MoveRequest
import com.example.model.response.GameResult
import com.example.model.response.MoveResult
import com.example.repository.GameHistoryRepository
import com.example.utils.GameRulesValidator
import com.example.utils.WinnerChecker.Companion.checkWinner
import kotlinx.coroutines.sync.withLock
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class MakeMoveUseCase(
    private val sessionManager: GameSessionManager,
    private val historyRepository: GameHistoryRepository
) {
    suspend operator fun invoke(sessionId: String, request: MoveRequest): ValidationResult<MoveResult> {
        val session = sessionManager.getSession(sessionId)
            ?: return ValidationResult.Error(
                GameErrorType.SESSION_NOT_FOUND,
                details = mapOf("sessionId" to sessionId)
            )

        sessionManager.ping(session.sessionId)

        return session.mutex.withLock {
            val state = session.state

            val validationResult = GameRulesValidator.validateMove(state, request)
            if (validationResult is ValidationResult.Error) return@withLock validationResult

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

            ValidationResult.Success(
                MoveResult(
                    board = updatedBoard,
                    currentTurn = if (winnerResult == WinnerResult.NONE) session.state.currentTurn else null,
                    winner = winnerName,
                    message = message,
                    isGameOver = winnerResult != WinnerResult.NONE
                )
            )
        }
    }

    private fun togglePlayer(current: String): String = if (current == "X") "O" else "X"
}