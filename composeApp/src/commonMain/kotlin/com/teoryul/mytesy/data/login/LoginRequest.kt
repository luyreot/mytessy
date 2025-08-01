package com.teoryul.mytesy.data.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
    val userID: String? = null,
    val userEmail: String? = null,
    val userPass: String? = null,
    val lang: String = "en"
)