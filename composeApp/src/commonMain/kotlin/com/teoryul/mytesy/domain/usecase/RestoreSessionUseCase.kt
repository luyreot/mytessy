package com.teoryul.mytesy.domain.usecase

import com.teoryul.mytesy.domain.session.SessionManager
import com.teoryul.mytesy.domain.session.SessionTable
import com.teoryul.mytesy.util.AppLogger.e
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RestoreSessionUseCase(
    private val sessionTable: SessionTable,
    private val sessionManager: SessionManager
) {

    suspend operator fun invoke(): SessionResult {
        return withContext(Dispatchers.Default) {
            try {
                val sessionData = sessionTable.loadSession()
                if (sessionData == null) {
                    return@withContext SessionResult.SessionNotFound
                }
                sessionManager.currentSession = sessionData
                return@withContext SessionResult.SessionRestored
            } catch (e: Exception) {
                e("❌ Unknown error: ${e.message}")
                return@withContext SessionResult.SessionNotFound
            }
        }
    }

    sealed class SessionResult() {
        data object SessionRestored : SessionResult()
        data object SessionNotFound : SessionResult()
    }
}