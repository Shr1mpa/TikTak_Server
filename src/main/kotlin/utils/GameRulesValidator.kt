package com.example.utils

import com.example.exceptions.CellAlreadyTakenException
import com.example.exceptions.GameAlreadyFinishedException
import com.example.exceptions.NotYourTurnException
import com.example.model.GameState
import com.example.model.WinnerResult
import com.example.model.request.MoveRequest

class GameRulesValidator {
    companion object {
        fun validateMove(state: GameState, request: MoveRequest) {
            if (state.winnerResult != WinnerResult.NONE) {
                val winnerName = when (state.winnerResult) {
                    WinnerResult.X, WinnerResult.O -> state.players[state.winnerResult.name] ?: state.winnerResult.name
                    WinnerResult.DRAW -> "нічия"
                    WinnerResult.NONE -> ""
                }
                throw GameAlreadyFinishedException("Гра завершена. Переможець: $winnerName")
            }

            if (request.player != state.currentTurn) throw NotYourTurnException()
            if (state.board[request.cell] != null) throw CellAlreadyTakenException()
        }
    }
}