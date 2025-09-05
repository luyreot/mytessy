package com.teoryul.mytesy.data.db

import app.cash.sqldelight.coroutines.asFlow
import com.teoryul.mytesy.data.api.model.appliance.OldAppDevicesResponse
import com.teoryul.mytesy.db.AppDatabase
import com.teoryul.mytesy.db.Appliance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ApplianceTable(databaseDriverFactory: DatabaseDriverFactory) {

    private val database = AppDatabase(databaseDriverFactory.createDriver())

    private val queries = database.applianceQueries

    fun getAllAppliances(): Flow<List<Appliance>> = queries.getAllAppliances()
        .asFlow()
        .map { it.executeAsList() }
        .flowOn(Dispatchers.IO)

    suspend fun insertAppliances(appliancesResponse: OldAppDevicesResponse) {
        withContext(Dispatchers.IO) {
            appliancesResponse.device?.forEach { (serial, appliance) ->
                queries.insertOrReplaceAppliance(
                    device_serial = serial,
                    url = appliance.url,
                    id = appliance.id,
                    group_name = appliance.deviceGroupName,
                    short_name = appliance.deviceShortName,
                    description = appliance.deviceDescription,
                    erp = appliance.deviceErp,
                    activity = appliance.deviceActivity.toLong(),
                    online = appliance.deviceOnline.toLong(),
                    set_mc = appliance.setMc?.toLong(),
                    status_id = appliance.deviceStatus.id,
                    status_text = appliance.deviceStatus.text,
                    status_tmpC = appliance.deviceStatus.tmpC,
                    status_tmpT = appliance.deviceStatus.tmpT,
                    status_tmpR = appliance.deviceStatus.tmpR,
                    status_pwr = appliance.deviceStatus.pwr,
                    status_mode = appliance.deviceStatus.mode,
                    status_err = appliance.deviceStatus.err,
                    status_wsw = appliance.deviceStatus.wsw,
                    status_ht = appliance.deviceStatus.ht,
                    status_tz = appliance.deviceStatus.tz,
                    status_wSSID = appliance.deviceStatus.wSSID,
                    status_wIP = appliance.deviceStatus.wIP,
                    status_date = appliance.deviceStatus.date,
                    status_prfl = appliance.deviceStatus.prfl,
                    status_extr = appliance.deviceStatus.extr,
                    status_hsw = appliance.deviceStatus.hsw,
                    status_reset = appliance.deviceStatus.reset,
                    status_wi = appliance.deviceStatus.wi,
                    status_wcntr = appliance.deviceStatus.wcntr,
                    status_bst = appliance.deviceStatus.bst,
                    status_vac = appliance.deviceStatus.vac,
                    status_prgVac = appliance.deviceStatus.prgVac,
                    status_prgP1MO = appliance.deviceStatus.prgP1MO,
                    status_prgP1TU = appliance.deviceStatus.prgP1TU,
                    status_prgP1WE = appliance.deviceStatus.prgP1WE,
                    status_prgP1TH = appliance.deviceStatus.prgP1TH,
                    status_prgP1FR = appliance.deviceStatus.prgP1FR,
                    status_prgP1SA = appliance.deviceStatus.prgP1SA,
                    status_prgP1SU = appliance.deviceStatus.prgP1SU,
                    status_prgP2MO = appliance.deviceStatus.prgP2MO,
                    status_prgP2TU = appliance.deviceStatus.prgP2TU,
                    status_prgP2WE = appliance.deviceStatus.prgP2WE,
                    status_prgP2TH = appliance.deviceStatus.prgP2TH,
                    status_prgP2FR = appliance.deviceStatus.prgP2FR,
                    status_prgP2SA = appliance.deviceStatus.prgP2SA,
                    status_prgP2SU = appliance.deviceStatus.prgP2SU,
                    status_prgP3MO = appliance.deviceStatus.prgP3MO,
                    status_prgP3TU = appliance.deviceStatus.prgP3TU,
                    status_prgP3WE = appliance.deviceStatus.prgP3WE,
                    status_prgP3TH = appliance.deviceStatus.prgP3TH,
                    status_prgP3FR = appliance.deviceStatus.prgP3FR,
                    status_prgP3SA = appliance.deviceStatus.prgP3SA,
                    status_prgP3SU = appliance.deviceStatus.prgP3SU,
                    status_pwcalc = appliance.deviceStatus.pwcalc,
                    status_pwc_t = appliance.deviceStatus.pwc_t,
                    status_pwc_u_date = appliance.deviceStatus.pwc_u?.date,
                    status_pwc_u_timestamp = appliance.deviceStatus.pwc_u?.timestamp,
                    status_pwc_u_utc = appliance.deviceStatus.pwc_u?.utc,
                    status_wtstp = appliance.deviceStatus.wtstp,
                    status_wup = appliance.deviceStatus.wup,
                    status_wdBm = appliance.deviceStatus.wdBm
                )
            }
        }
    }

    suspend fun deleteAppliance(serial: String) {
        withContext(Dispatchers.IO) {
            queries.deleteApplianceBySerial(serial)
        }
    }

    suspend fun deleteAllAppliances() {
        withContext(Dispatchers.IO) {
            queries.deleteAllAppliances()
        }
    }
}