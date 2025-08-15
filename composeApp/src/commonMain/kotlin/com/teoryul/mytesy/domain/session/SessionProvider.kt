package com.teoryul.mytesy.domain.session

interface SessionProvider {

    val current: SessionData?
    
    suspend fun require(): SessionData = current ?: throw UnauthenticatedException()
}