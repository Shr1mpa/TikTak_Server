package com.example.model

import com.example.utils.initEmptyBoard

class GameStateHolder {

    var state: GameState = createNewGame()

    fun reset() {
        state = createNewGame()
    }

    private fun createNewGame(): GameState {
        return GameState(
            board = initEmptyBoard(),
            currentTurn = "X",
            players = emptyMap()
        )
    }
}