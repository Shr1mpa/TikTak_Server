package com.example.model

enum class GameMessage(val key: String) {
    DRAW("game.draw"),
    WINNER("game.winner"),
    PLAYER_TURN("game.turn")
}