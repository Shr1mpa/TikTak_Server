package utils

import com.example.manager.GameSessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class LobbyCleaner(
    private val sessionManager: GameSessionManager
) {
    private val scope = CoroutineScope(Dispatchers.Default)
    private val ttl = 60_000L // 60 секунд

    fun start() {
        scope.launch {
            while (isActive) {
                delay(10_000)
                sessionManager.removeInactiveLobbies(ttl)
            }
        }
    }
}