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
import com.example.repository.impl.PostgresGameHistoryRepository
import com.example.usecase.*
import org.koin.dsl.module
import usecase.LeaveLobbyUseCase
import utils.LobbyCleaner

val appModule = module {
    single<GameSessionManager> { GameSessionManager() }
    single { LobbyCleaner(get()) }
    single { CreateLobbyUseCase(get()) }
    single { ListAvailableLobbiesUseCase(get()) }
    single { GetLobbyPlayersUseCase(get()) }
    single { LeaveLobbyUseCase(get()) }
    single { AppConfigLoader.load() }
    single<GameHistoryRepository> { PostgresGameHistoryRepository() }

    single<DatabaseConnectionFactory> { PostgresDatabaseConnectionFactoryImpl(get()) }
    single<ExposedConnectionFactory> { PostgresExposedConnectionFactoryImpl(get()) }

    factory { GetCurrentTurnUseCase(get()) }
    factory { GetGameStateUseCase(get()) }
    factory { GetGameHistoryUseCase(get()) }
    factory { MakeMoveUseCase(get(), get()) }
    factory { JoinGameUseCase(get()) }
    factory { LobbyController(get(), get(), get(), get(), get()) }
    factory { GameController(get(), get(), get(), get()) }
}