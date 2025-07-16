package com.example.repository.impl

import com.example.model.response.GameResult
import com.example.repository.GameHistoryRepository
import kotlinx.serialization.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.*
import java.io.File

class FileGameHistoryRepository(
    private val file: File = File("game_history.json")
) : GameHistoryRepository {
    private val json = Json { prettyPrint = true; encodeDefaults = true }

    private val cachedResults = mutableListOf<GameResult>().apply {
        if (file.exists()) {
            val text = file.readText()
            if (text.isNotBlank()) {
                addAll(json.decodeFromString(ListSerializer(GameResult.serializer()), text))
            }
        }
    }

    override fun save(result: GameResult) {
        cachedResults.add(result)
        file.writeText(json.encodeToString(ListSerializer(GameResult.serializer()), cachedResults))
    }

    override fun getAll(): List<GameResult> = cachedResults.toList()
}