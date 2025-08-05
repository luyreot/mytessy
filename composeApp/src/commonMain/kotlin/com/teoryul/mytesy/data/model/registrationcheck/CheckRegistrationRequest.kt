package com.teoryul.mytesy.data.model.registrationcheck

import kotlinx.serialization.Serializable

@Serializable
data class CheckRegistrationRequest(
    val email: String,
    val userID: String? = null,
    val userEmail: String? = null,
    val userPass: String? = null,
    val lang: String = "en"
)