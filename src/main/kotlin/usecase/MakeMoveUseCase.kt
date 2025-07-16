package com.example.usecase

import com.example.model.GameStateHolder
import com.example.model.WinnerResult
import com.example.model.request.MoveRequest
import com.example.model.response.GameResult
import com.example.model.response.MoveResult
import com.example.repository.GameHistoryRepository
import com.example.utils.GameRulesValidator
import com.example.utils.WinnerChecker.Companion.checkWinner

class MakeMoveUseCase(
    private val gameStateHolder: GameStateHolder,
    private val historyRepository: GameHistoryRepository
) {
    operator fun invoke(request: MoveRequest): Result<MoveResult> {
        val state = gameStateHolder.state

        GameRulesValidator.validateMove(state, request)

        val updatedBoard = state.board.toMutableMap()
        updatedBoard[request.cell] = request.player

        val winnerResult = checkWinner(updatedBoard)
        val winnerName = when (winnerResult) {
            WinnerResult.X, WinnerResult.O -> state.players[winnerResult.name]
            WinnerResult.DRAW, WinnerResult.NONE -> ""
        }

        gameStateHolder.state = state.copy(
            board = updatedBoard,
            currentTurn = if (winnerResult == WinnerResult.NONE) togglePlayer(request.player) else state.currentTurn,
            winnerResult = winnerResult
        )

        if (winnerResult != WinnerResult.NONE) {
            historyRepository.save(
                GameResult(
                    players = state.players,
                    winner = winnerName ?: "",
                    finalBoard = updatedBoard
                )
            )
        }

        val message = when (winnerResult) {
            WinnerResult.X, WinnerResult.O -> "Гру виграв $winnerName"
            WinnerResult.DRAW -> "Гра завершена в нічию"
            WinnerResult.NONE -> "Хід прийнято"
        }

        return Result.success(
            MoveResult(
                board = updatedBoard,
                currentTurn = gameStateHolder.state.currentTurn,
                winner = winnerName,
                message = message
            )
        )
    }

    private fun togglePlayer(current: String): String = if (current == "X") "O" else "X"
}
