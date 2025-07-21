package database.models

import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.json.jsonb
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object GameHistoryTable : Table("game_history") {
    val id = integer("id").autoIncrement()
    val sessionId = text("session_id")
    val playerX = text("player_x").nullable()
    val playerO = text("player_o").nullable()
    val moves = jsonb<Map<String, String?>>("moves", Json.Default)
    val winner = text("winner").nullable()
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(id)
}