package com.teoryul.mytesy.domain.usecase

import com.teoryul.mytesy.domain.appliance.ApplianceEntity
import com.teoryul.mytesy.domain.model.ErrorResult
import com.teoryul.mytesy.domain.model.ErrorResultMapper
import com.teoryul.mytesy.domain.model.toErrorResult
import com.teoryul.mytesy.domain.repo.ApplianceRepository
import com.teoryul.mytesy.infra.AppLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class ApplianceUseCase(
    private val applianceRepository: ApplianceRepository,
    private val errorMapper: ErrorResultMapper
) {

    fun observeAppliances(): Flow<List<ApplianceEntity>> {
        return applianceRepository.observeAppliances()
    }

    suspend fun syncAppliances(): Result {
        return withContext(Dispatchers.Default) {
            try {
                applianceRepository.refreshAppliances()
                return@withContext Result.Success
            } catch (t: Throwable) {
                val error = t.toErrorResult(errorMapper)
                AppLogger.e(error.message)
                return@withContext Result.Fail(error)
            }
        }
    }

    suspend fun toggleAppliancePower(appliance: ApplianceEntity, enabled: Boolean): Result {
        return withContext(Dispatchers.Default) {
            try {
                applianceRepository.toggleAppliancePower(
                    applianceId = appliance.id.orEmpty(),
                    enabled = enabled
                )
                return@withContext Result.Success
            } catch (t: Throwable) {
                val error = t.toErrorResult(errorMapper)
                AppLogger.e(error.message)
                return@withContext Result.Fail(error)
            }
        }
    }

    suspend fun deleteAppliance(deviceSerial: String): Result {
        return withContext(Dispatchers.Default) {
            try {
                applianceRepository.deleteAppliance(deviceSerial)
                return@withContext Result.Success
            } catch (t: Throwable) {
                val error = t.toErrorResult(errorMapper)
                AppLogger.e(error.message)
                return@withContext Result.Fail(error)
            }
        }
    }

    suspend fun deleteAllAppliances(): Result {
        return withContext(Dispatchers.Default) {
            try {
                applianceRepository.deleteAllAppliances()
                return@withContext Result.Success
            } catch (t: Throwable) {
                val error = t.toErrorResult(errorMapper)
                AppLogger.e(error.message)
                return@withContext Result.Fail(error)
            }
        }
    }

    sealed class Result {
        data object Success : Result()
        data class Fail(val error: ErrorResult) : Result()
    }
}