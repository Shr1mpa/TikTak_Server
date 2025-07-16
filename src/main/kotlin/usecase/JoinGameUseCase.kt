package com.example.usecase

import com.example.model.GameStateHolder
import com.example.model.response.JoinResponse
import com.example.model.request.PlayerJoinRequest
import com.example.repository.impl.PlayerRepository

class JoinGameUseCase(
    private val playerRepository: PlayerRepository,
    private val gameStateHolder: GameStateHolder) {
    operator fun invoke(request: PlayerJoinRequest): Result<JoinResponse> {
        val name = request.name.trim()

        playerRepository.findSymbolByName(name)?.let { existingSymbol ->
            return Result.success(
                JoinResponse(name, existingSymbol, "Ви вже приєднались як $existingSymbol")
            )
        }

        val assigned = when {
            playerRepository.isAvailable("X") -> "X"
            playerRepository.isAvailable("O") -> "O"
            else -> return Result.failure(IllegalStateException("Гра вже має двох гравців"))
        }

        playerRepository.assignPlayer(assigned, name)

        gameStateHolder.state = gameStateHolder.state.copy(
            players = playerRepository.getAll()
        )

        return Result.success(
            JoinResponse(name, assigned, "Гравець успішно приєднався як $assigned")
        )
    }
}