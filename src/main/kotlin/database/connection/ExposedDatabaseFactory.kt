package com.example.database.connection

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object ExposedDatabaseFactory {
    fun init(exposedConnectionFactory: ExposedConnectionFactory) {
        transaction(exposedConnectionFactory.getDatabase()) {
            // Example: SchemaUtils.create(GameHistoryTable)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}