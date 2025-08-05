package com.teoryul.mytesy.data.api.model.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val userID: Int? = null,
    val password: String? = null,
    val email: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val lang: String? = null,
    val token: String? = null,
    val oldAppEmail: String? = null,
    val oldAppPassword: String? = null,
    val errors: Map<String, String>? = null
)