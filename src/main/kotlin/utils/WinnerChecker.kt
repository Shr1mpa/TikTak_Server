package com.example.utils

import com.example.model.WinnerResult

class WinnerChecker {
    companion object {
        fun checkWinner(board: Map<String, String?>): WinnerResult {
            val winPatterns = listOf(
                listOf("A1", "A2", "A3"),
                listOf("B1", "B2", "B3"),
                listOf("A1", "B1", "C1"),
                listOf("A2", "B2", "C2"),
                listOf("A3", "B3", "C3"),
                listOf("C1", "C2", "C3"),
                listOf("A1", "B2", "C3"),
                listOf("A3", "B2", "C1"),
            )

            for (pattern in winPatterns) {
                val (a, b, c) = pattern
                val symbol = board[a]
                if (symbol != null && symbol == board[b] && symbol == board[c]) {
                    return when (symbol) {
                        "X" -> WinnerResult.X
                        "O" -> WinnerResult.O
                        else -> WinnerResult.NONE
                    }
                }
            }

            return if (board.values.none { it == null }) {
                WinnerResult.DRAW
            } else {
                WinnerResult.NONE
            }
        }
    }
}