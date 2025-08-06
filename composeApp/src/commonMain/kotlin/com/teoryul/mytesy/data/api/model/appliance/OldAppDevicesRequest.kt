package com.teoryul.mytesy.data.api.model.appliance

import kotlinx.serialization.Serializable

@Serializable
data class OldAppDevicesRequest(
    val ALT: String,
    val CURRENT_SESSION: String? = null,
    val PHPSESSID: String,
    val lang: String,
    val last_login_username: String,
    val userEmail: String,
    val userID: Long,
    val userPass: String
)