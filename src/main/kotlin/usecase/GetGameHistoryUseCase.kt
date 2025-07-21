package com.example.usecase

import com.example.model.response.GameResult
import com.example.repository.GameHistoryRepository

class GetGameHistoryUseCase(
    private val repository: GameHistoryRepository
) {
    suspend operator fun invoke(): List<GameResult> {
        return repository.getAll()
    }
}