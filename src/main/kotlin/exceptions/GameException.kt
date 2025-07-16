package com.example.exceptions

sealed class GameException(message: String) : RuntimeException(message)

class GameAlreadyFinishedException(winner: String?) :
    GameException("Гра завершена. Переможець: $winner")

class NotYourTurnException :
    GameException("Зараз не ваша черга")

class CellAlreadyTakenException :
    GameException("Клітинка вже зайнята")