package com.teoryul.mytesy.data.api.model.registrationcheck

import kotlinx.serialization.Serializable

@Serializable
data class CheckRegistrationResponse(
    val success: Boolean? = null,
    val errors: Map<String, List<String>>? = null
)