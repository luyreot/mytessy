package com.teoryul.mytesy.domain.session

/**
 * TODO
 * Android: store tokens only in DB; if you must store credentials, use EncryptedSharedPreferences (backed by Keystore).
 * iOS: use Keychain for credentials; DB for non-sensitive tokens.
 */
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