package com.teoryul.mytesy.domain.session

interface SessionProvider {

    val current: SessionData?

    @Throws(UnauthenticatedException::class)
    suspend fun require(): SessionData = current ?: throw UnauthenticatedException()
}