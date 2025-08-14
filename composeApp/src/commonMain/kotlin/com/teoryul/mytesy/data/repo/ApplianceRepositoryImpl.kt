package com.teoryul.mytesy.data.repo

import com.teoryul.mytesy.data.api.ApiService
import com.teoryul.mytesy.data.db.ApplianceTable
import com.teoryul.mytesy.data.db.toEntity
import com.teoryul.mytesy.domain.appliance.ApplianceEntity
import com.teoryul.mytesy.domain.repo.ApplianceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

        if (response.device.isNullOrEmpty()) {
            applianceTable.deleteAllAppliances()
        } else {
            applianceTable.insertAppliances(response)
        }
    }

    override suspend fun deleteAppliance(serial: String) {
        applianceTable.deleteAppliance(serial)
    }

    override suspend fun deleteAllAppliances() {
        applianceTable.deleteAllAppliances()
    }
}