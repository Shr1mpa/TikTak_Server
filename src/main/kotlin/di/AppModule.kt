package com.example.di

import com.example.controller.GameController
import com.example.controller.SessionController
import com.example.manager.GameSessionManager
import com.example.repository.GameHistoryRepository
import com.example.repository.impl.FileGameHistoryRepository
import com.example.usecase.GetCurrentTurnUseCase
import com.example.usecase.GetGameHistoryUseCase
import com.example.usecase.GetGameStateUseCase
import com.example.usecase.JoinGameUseCase
import com.example.usecase.MakeMoveUseCase
import org.koin.dsl.module

val appModule = module {
    single { GameSessionManager() }

    single<GameHistoryRepository> { FileGameHistoryRepository() }

    factory { GetCurrentTurnUseCase(get()) }
    factory { GetGameStateUseCase(get()) }
    factory { GetGameHistoryUseCase(get()) }
    factory { MakeMoveUseCase(get(), get()) }
    factory { JoinGameUseCase(get()) }

    factory { SessionController(get()) }
    factory { GameController(get(), get(), get(), get(), get()) } // 5 use-case'ів
}