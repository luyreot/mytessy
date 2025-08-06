package com.teoryul.mytesy.domain.session

data class SessionData(
    val token: String,
    val accSession: String,
    val accAlt: String,
    val userId: Long,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val lang: String
)