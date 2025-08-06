package com.teoryul.mytesy.data.api.model.appliance

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OldAppDevicesResponse(
    val device: Map<String, OldApplianceResponse>? = null
)

@Serializable
data class OldApplianceResponse(
    @SerialName("URL") val url: String,
    @SerialName("id") val id: String,
    @SerialName("DeviceSerial") val deviceSerial: String,
    @SerialName("DeviceGroupName") val deviceGroupName: String? = null,
    @SerialName("DeviceShortName") val deviceShortName: String? = null,
    @SerialName("DeviceDescription") val deviceDescription: String? = null,
    @SerialName("DeviceERP") val deviceErp: String? = null,
    @SerialName("DeviceActivity") val deviceActivity: Int,
    @SerialName("DeviceOnline") val deviceOnline: Int,
    @SerialName("DeviceStatus") val deviceStatus: OldDeviceStatusResponse,
    @SerialName("SetMC") val setMc: Int? = null
)

@Serializable
data class OldDeviceStatusResponse(
    val tz: String? = null,
    val wsw: String? = null,
    val prfl: String? = null,
    val extr: String? = null,
    val id: String? = null,
    val hsw: String? = null,
    val reset: String? = null,
    val wi: String? = null,
    val err: String? = null,
    val tmpT: String? = null,
    val tmpR: String? = null,
    val mode: String? = null,
    val wcntr: String? = null,
    val bst: String? = null,
    val vac: String? = null,
    val pwr: String? = null,
    val ht: String? = null,
    val tmpC: String? = null,

    // Programs for 3 profiles, 7 days a week
    val prgVac: String? = null,
    val prgP1MO: String? = null,
    val prgP1TU: String? = null,
    val prgP1WE: String? = null,
    val prgP1TH: String? = null,
    val prgP1FR: String? = null,
    val prgP1SA: String? = null,
    val prgP1SU: String? = null,
    val prgP2MO: String? = null,
    val prgP2TU: String? = null,
    val prgP2WE: String? = null,
    val prgP2TH: String? = null,
    val prgP2FR: String? = null,
    val prgP2SA: String? = null,
    val prgP2SU: String? = null,
    val prgP3MO: String? = null,
    val prgP3TU: String? = null,
    val prgP3WE: String? = null,
    val prgP3TH: String? = null,
    val prgP3FR: String? = null,
    val prgP3SA: String? = null,
    val prgP3SU: String? = null,

    val pwcalc: String? = null,
    val pwc_t: String? = null,

    val pwc_u: OldPwcuResponse? = null,

    val wIP: String? = null,
    val wSSID: String? = null,
    val date: String? = null,
    val wtstp: String? = null,
    val wup: String? = null,
    val wdBm: String? = null,
    @SerialName("Text") val text: String? = null
)

@Serializable
data class OldPwcuResponse(
    val date: String? = null,
    val timestamp: String? = null,
    val utc: String? = null
)