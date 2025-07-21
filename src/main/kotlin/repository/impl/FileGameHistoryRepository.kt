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

    private val json = Json {
        prettyPrint = true
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    private val cachedResults = mutableListOf<GameResult>().apply {
        if (file.exists()) {
            val text = file.readText()
            if (text.isNotBlank()) {
                runCatching {
                    addAll(json.decodeFromString(ListSerializer(GameResult.serializer()), text))
                }.onFailure {
                    println("Помилка при зчитуванні історії гри: ${it.message}")
                }
            }
        }
    }

    override suspend fun save(result: GameResult) {
        cachedResults.add(result)
        saveToFile()
    }

    override suspend  fun getAll(): List<GameResult> = cachedResults.toList()

    private fun saveToFile() {
        runCatching {
            file.writeText(json.encodeToString(ListSerializer(GameResult.serializer()), cachedResults))
        }.onFailure {
            println("Помилка при збереженні історії гри: ${it.message}")
        }
    }
}