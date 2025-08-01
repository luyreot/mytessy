package com.teoryul.mytesy.data.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val userID: Int,
    val password: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val lang: String,
    val token: String,
    val oldAppEmail: String,
    val oldAppPassword: String
)