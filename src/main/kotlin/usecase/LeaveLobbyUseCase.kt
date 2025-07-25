package usecase

import com.example.exceptions.GameErrorType
import com.example.exceptions.LeavePlayerResult
import com.example.exceptions.ValidationResult
import com.example.manager.GameSessionManager
import com.example.model.response.LeaveResponse

class LeaveLobbyUseCase(
    private val sessionManager: GameSessionManager
) {
    operator fun invoke(sessionId: String, playerName: String): ValidationResult<LeaveResponse> {
        return when (val result = sessionManager.removePlayerFromSession(sessionId, playerName)) {
            LeavePlayerResult.Success -> ValidationResult.Success(
                LeaveResponse("Player $playerName has been successfully removed")
            )
            LeavePlayerResult.SessionNotFound -> ValidationResult.Error(
                GameErrorType.SESSION_NOT_FOUND,
                details = mapOf("sessionId" to sessionId, "player" to playerName)
            )
        }
    }
}