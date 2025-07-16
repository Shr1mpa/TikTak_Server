package com.example.usecase

import com.example.model.GameStateHolder
import com.example.model.response.CurrentTurnDto

class GetCurrentTurnUseCase(
    private val gameStateHolder: GameStateHolder
) {
    operator fun invoke(): CurrentTurnDto {
        return CurrentTurnDto(currentTurn = gameStateHolder.state.currentTurn)
    }
}