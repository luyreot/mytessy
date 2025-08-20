package com.teoryul.mytesy.domain.usecase

import com.teoryul.mytesy.data.api.ApiService
import com.teoryul.mytesy.domain.session.SessionManager
import com.teoryul.mytesy.infra.AppLogger
import com.teoryul.mytesy.infra.AppLogger.e
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RestoreSessionUseCase(
    private val api: ApiService,
    private val sessionManager: SessionManager
) {

    suspend operator fun invoke(): Result {
        return withContext(Dispatchers.Default) {
            try {
                val sessionData = sessionManager.restore()
                if (sessionData == null) {
                    return@withContext Result.SessionNotFound
                }

                val oldLoginResponse = api.loginOldApp(
                    email = sessionData.email,
                    password = sessionData.password,
                    userID = sessionData.userId,
                    userEmail = sessionData.email,
                    userPass = sessionData.password
                )

                if (oldLoginResponse.error != null) {
                    AppLogger.d(oldLoginResponse.error)
                    return@withContext Result.SessionNotFound
                }

                if (oldLoginResponse.accSession.isNullOrEmpty() ||
                    oldLoginResponse.accAlt.isNullOrEmpty()
                ) {
                    AppLogger.d("Missing session parameters")
                    return@withContext Result.SessionNotFound
                }

                sessionManager.set(
                    sessionData.copy(
                        accSession = oldLoginResponse.accSession,
                        accAlt = oldLoginResponse.accAlt
                    )
                )
                return@withContext Result.SessionRestored
            } catch (e: Exception) {
                e("❌ Unknown error: ${e.message}")
                return@withContext Result.SessionNotFound
            }
        }
    }

    sealed class Result {
        data object SessionRestored : Result()
        data object SessionNotFound : Result()
    }
}