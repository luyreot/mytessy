package com.teoryul.mytesy.data.api.model.appliance

import com.teoryul.mytesy.data.api.model.RequiresSession
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SendApplianceCommandRequest(
    @SerialName("ALT")
    val alt: String,
    @SerialName("CURRENT_SESSION")
    val currentSession: String? = null,
    @SerialName("PHPSESSID")
    val phpSessId: String,
    val lang: String,
    @SerialName("last_login_username")
    val lastLoginUsername: String,
    val userEmail: String,
    val userID: Long,
    val userPass: String,
    @SerialName("id")
    val applianceId: String,
    val apiVersion: String = "apiv2",
    val command: String,
    @SerialName("value")
    val commandValue: String
) : RequiresSession