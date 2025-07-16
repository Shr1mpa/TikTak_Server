package com.example.di

import com.example.controller.AuthorizeController
import com.example.controller.GameController
import com.example.model.GameStateHolder
import com.example.repository.GameHistoryRepository
import com.example.repository.impl.FileGameHistoryRepository
import com.example.repository.impl.PlayerRepository
import com.example.usecase.GetCurrentTurnUseCase
import com.example.usecase.GetGameHistoryUseCase
import com.example.usecase.JoinGameUseCase
import com.example.usecase.MakeMoveUseCase
import org.koin.dsl.module

val appModule = module {
    single { GameStateHolder() }
    single { PlayerRepository() }
    factory { GetCurrentTurnUseCase(get()) }
    single<GameHistoryRepository> { FileGameHistoryRepository() }
    factory { GetGameHistoryUseCase(get()) }
    factory { MakeMoveUseCase(get(), get()) }
    factory { JoinGameUseCase(get(), get()) }
    factory { AuthorizeController(get()) }
    factory { GameController(get(), get(), get()) }
}