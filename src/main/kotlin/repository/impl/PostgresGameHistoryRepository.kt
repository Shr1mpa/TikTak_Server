package com.example.repository.impl
import com.example.database.connection.ExposedDatabaseFactory.dbQuery
import com.example.model.response.GameResult
import com.example.repository.GameHistoryRepository
import database.models.GameHistoryTable
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

class PostgresGameHistoryRepository : GameHistoryRepository {

    override suspend fun save(result: GameResult) {
        val playerX = result.players["X"] ?: "Unknown"
        val playerO = result.players["O"] ?: "Unknown"

        dbQuery {
            GameHistoryTable.insert {
                it[sessionId] = result.sessionId
                it[GameHistoryTable.playerX] = playerX
                it[GameHistoryTable.playerO] = playerO
                it[moves] = result.board
                it[winner] = result.winner
                it[endedAt] = Clock.System.now().toLocalDateTime(TimeZone.UTC)
            }
        }
    }

    override suspend fun getAll(): List<GameResult> = dbQuery {
        GameHistoryTable.selectAll().map {
            GameResult(
                sessionId = it[GameHistoryTable.sessionId],
                players = mapOf(
                    "X" to it[GameHistoryTable.playerX],
                    "O" to it[GameHistoryTable.playerO]
                ),
                board = it[GameHistoryTable.moves],
                winner = it[GameHistoryTable.winner],
                endedAt = it[GameHistoryTable.endedAt].toString()
            )
        }
    }
}