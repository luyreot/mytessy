package com.teoryul.mytesy.data.model.oldlogin

import kotlinx.serialization.Serializable

@Serializable
data class OldAppLoginRequest(
    val email: String,
    val password: String,
    val userID: Int?,
    val userEmail: String?,
    val userPass: String?,
    val lang: String = "en"
)