package usecase

import com.example.exceptions.GameErrorType
import com.example.exceptions.LeavePlayerResult
import com.example.exceptions.ValidationResult
import com.example.manager.GameSessionManager

class LeaveLobbyUseCase(
    private val sessionManager: GameSessionManager
) {
    operator fun invoke(sessionId: String, playerName: String): ValidationResult<String> {
        return when (val result = sessionManager.removePlayerFromSession(sessionId, playerName)) {
            LeavePlayerResult.Success -> ValidationResult.Success("Player $playerName left the lobby")
            LeavePlayerResult.SessionNotFound -> ValidationResult.Error(
                GameErrorType.SESSION_NOT_FOUND,
                details = mapOf("sessionId" to sessionId, "player" to playerName)
            )
        }
    }
}