package com.teoryul.mytesy.domain.repo

import com.teoryul.mytesy.domain.appliance.ApplianceEntity
import kotlinx.coroutines.flow.Flow

interface ApplianceRepository {

    /**
     * Observes all appliances stored locally.
     */
    fun observeAppliances(): Flow<List<ApplianceEntity>>

    /**
     * Fetches appliances from the backend and updates the local database.
     * If the backend returns an empty list, clears the local appliance data.
     */
    suspend fun refreshAppliances()

    /**
     * Deletes a single appliance from the local database using its serial.
     */
    suspend fun deleteAppliance(serial: String)

    /**
     * Deletes all appliances from the local database.
     */
    suspend fun deleteAllAppliances()
}