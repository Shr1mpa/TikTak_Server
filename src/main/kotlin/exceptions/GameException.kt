package com.example.exceptions
import kotlinx.serialization.Serializable

sealed class GameException(message: String) : RuntimeException(message)

class GameAlreadyFinishedException(winner: String?) :
    GameException("Гра завершена. Переможець: $winner")

class NotYourTurnException :
    GameException("Зараз не ваша черга")

class CellAlreadyTakenException :
    GameException("Клітинка вже зайнята")


class ValidationException(message: String) : IllegalArgumentException(message)
class NotFoundException(message: String) : Exception(message)