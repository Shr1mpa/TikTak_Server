package com.example.repository

import com.example.model.response.GameResult

interface GameHistoryRepository {
    suspend fun save(result: GameResult)
    suspend fun getAll(): List<GameResult>
}