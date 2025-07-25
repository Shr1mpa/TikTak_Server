package com.example.utils

import com.example.exceptions.GameErrorType
import com.example.exceptions.ValidationResult
import com.example.model.GameState
import com.example.model.WinnerResult
import com.example.model.request.MoveRequest

class GameRulesValidator {
    companion object {
        fun validateMove(state: GameState, request: MoveRequest): ValidationResult<Unit> {
            if (state.winnerResult != WinnerResult.NONE) {
                val winnerName = when (state.winnerResult) {
                    WinnerResult.X, WinnerResult.O -> state.players[state.winnerResult.name] ?: state.winnerResult.name
                    WinnerResult.DRAW -> "DRAW"
                    WinnerResult.NONE -> ""
                }
                return ValidationResult.Error(
                    type = GameErrorType.GAME_ALREADY_FINISHED,
                    details = mapOf("winner" to winnerName)
                )
            }

            if (request.player != state.currentTurn) {
                return ValidationResult.Error(
                    type = GameErrorType.NOT_YOUR_TURN,
                    details = mapOf("player" to request.player, "currentTurn" to state.currentTurn)
                )
            }

            if (state.board[request.cell] != null) {
                return ValidationResult.Error(
                    type = GameErrorType.CELL_ALREADY_TAKEN,
                    details = mapOf("cell" to request.cell)
                )
            }

            return ValidationResult.Success(Unit)
        }
    }
}