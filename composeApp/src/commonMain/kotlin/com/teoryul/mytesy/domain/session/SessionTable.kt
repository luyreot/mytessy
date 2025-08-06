package com.teoryul.mytesy.domain.session

interface SessionTable {
    suspend fun loadSession(): SessionData?
    suspend fun saveSession(data: SessionData)
    suspend fun clearSession()
}