package com.teoryul.mytesy.domain.usecase

import com.teoryul.mytesy.domain.appliance.ApplianceEntity
import com.teoryul.mytesy.domain.repo.ApplianceRepository
import com.teoryul.mytesy.domain.session.SessionTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ApplianceUseCase(
    private val sessionTable: SessionTable,
    private val applianceRepository: ApplianceRepository
) {

    fun observeAppliances(): Flow<List<ApplianceEntity>> {
        return applianceRepository.observeAppliances()
    }

    // todo
    suspend fun syncAppliances(): Result<Unit> {
        return withContext(Dispatchers.Default) {
            val session = sessionTable.loadSession()

            if (session == null) {
                return@withContext Result.failure(IllegalStateException("No session available"))
            }

            return@withContext try {
                applianceRepository.refreshAppliances(
                    alt = session.accAlt,
                    currentSession = null,
                    phpSessId = session.accSession,
                    lang = session.lang,
                    lastLoginUsername = session.email,
                    userEmail = session.email,
                    userID = session.userId,
                    userPass = session.password
                )
                Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun deleteAppliance(deviceSerial: String) {
        withContext(Dispatchers.Default) {
            applianceRepository.deleteAppliance(deviceSerial)
        }
    }

    suspend fun deleteAllAppliances() {
        withContext(Dispatchers.Default) {
            applianceRepository.deleteAllAppliances()
        }
    }
}