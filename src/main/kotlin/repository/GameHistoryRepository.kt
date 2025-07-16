package com.example.repository

import com.example.model.response.GameResult

interface GameHistoryRepository {
    fun save(result: GameResult)
    fun getAll(): List<GameResult>
}