package com.teoryul.mytesy.domain.usecase

import com.teoryul.mytesy.domain.appliance.ApplianceEntity
import com.teoryul.mytesy.domain.model.ErrorResult
import com.teoryul.mytesy.domain.model.toErrorResult
import com.teoryul.mytesy.domain.repo.ApplianceRepository
import com.teoryul.mytesy.util.AppLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ApplianceUseCase(
    private val applianceRepository: ApplianceRepository
) {

    fun observeAppliances(): Flow<List<ApplianceEntity>> {
        return applianceRepository.observeAppliances()
    }

    suspend fun syncAppliances(): ApplianceResult {
        return withContext(Dispatchers.Default) {
            return@withContext try {
                applianceRepository.refreshAppliances()
                ApplianceResult.Success
            } catch (t: Throwable) {
                val error = t.toErrorResult()
                AppLogger.e(error.message)
                ApplianceResult.Fail(error)
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


    sealed class ApplianceResult {

        data object Success : ApplianceResult()

        data class Fail(val error: ErrorResult) : ApplianceResult()
    }
}