package com.teoryul.mytesy.data.repo

import com.teoryul.mytesy.data.api.ApiService
import com.teoryul.mytesy.data.db.ApplianceTable
import com.teoryul.mytesy.data.db.toEntity
import com.teoryul.mytesy.domain.appliance.ApplianceEntity
import com.teoryul.mytesy.domain.repo.ApplianceRepository
import com.teoryul.mytesy.data.common.UnauthenticatedException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * TODO Add method to delete appliance from server
 */
class ApplianceRepositoryImpl(
    private val api: ApiService,
    private val applianceTable: ApplianceTable
) : ApplianceRepository {

    override fun observeAppliances(): Flow<List<ApplianceEntity>> {
        return applianceTable.getAllAppliances().map { applianceList ->
            applianceList.map { it.toEntity() }
        }
    }

    override suspend fun refreshAppliances() {
        val response = api.getOldAppDevices()

        // Invalid session
        if (response.device == null) {
            throw UnauthenticatedException()
        }

        if (response.device.isEmpty()) {
            deleteAllAppliances()
            return
        }

        applianceTable.insertAppliances(response)
    }

    override suspend fun deleteAppliance(serial: String) {
        applianceTable.deleteAppliance(serial)
    }

    override suspend fun deleteAllAppliances() {
        applianceTable.deleteAllAppliances()
    }
}