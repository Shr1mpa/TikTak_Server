package com.example.di

import com.example.controller.GameController
import com.example.controller.LobbyController
import com.example.database.config.AppConfigLoader
import com.example.database.connection.DatabaseConnectionFactory
import com.example.database.connection.ExposedConnectionFactory
import com.example.database.connection.impl.PostgresDatabaseConnectionFactoryImpl
import com.example.database.connection.impl.PostgresExposedConnectionFactoryImpl
import com.example.manager.GameSessionManager
import com.example.repository.GameHistoryRepository
import com.example.repository.impl.FileGameHistoryRepository
import com.example.repository.impl.PostgresGameHistoryRepository
import com.example.usecase.GetCurrentTurnUseCase
import com.example.usecase.GetGameHistoryUseCase
import com.example.usecase.GetGameStateUseCase
import com.example.usecase.JoinGameUseCase
import com.example.usecase.MakeMoveUseCase
import org.koin.dsl.module

val appModule = module {
    single { GameSessionManager() }
    single { AppConfigLoader.load() }
    single<GameHistoryRepository> { PostgresGameHistoryRepository() }

    single<DatabaseConnectionFactory> { PostgresDatabaseConnectionFactoryImpl(get()) }
    single<ExposedConnectionFactory> { PostgresExposedConnectionFactoryImpl(get()) }

    factory { GetCurrentTurnUseCase(get()) }
    factory { GetGameStateUseCase(get()) }
    factory { GetGameHistoryUseCase(get()) }
    factory { MakeMoveUseCase(get(), get()) }
    factory { JoinGameUseCase(get()) }
    factory { LobbyController(get(), get()) }
    factory { GameController(get(), get(), get(), get()) }
}