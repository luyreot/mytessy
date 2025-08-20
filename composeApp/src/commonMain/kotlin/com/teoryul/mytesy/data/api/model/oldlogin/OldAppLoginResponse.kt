package com.teoryul.mytesy.data.api.model.oldlogin

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OldAppLoginResponse(
    @SerialName("acc_alt")
    val accAlt: String? = null,
    @SerialName("acc_session")
    val accSession: String? = null,
    val error: String? = null
)