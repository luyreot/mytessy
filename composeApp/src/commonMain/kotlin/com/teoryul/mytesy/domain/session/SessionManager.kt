package com.teoryul.mytesy.domain.session

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SessionManager(
    appScope: CoroutineScope,
    private val sessionTable: SessionTable
) : SessionProvider {

    private val writeMutex = Mutex()

    private val _session = MutableStateFlow<SessionData?>(null)

    /**
     * Hot, in-memory session for the whole app runtime.
     */
    val session: StateFlow<SessionData?> = _session

    override val current: SessionData? get() = session.value

    /**
     * Two-state view for coordinators/pollers/UI routing.
     */
    val status: StateFlow<SessionStatus> =
        session
            .map { if (it == null) SessionStatus.LOGGED_OUT else SessionStatus.LOGGED_IN }
            .distinctUntilChanged()
            .stateIn(appScope, SharingStarted.Eagerly, SessionStatus.LOGGED_OUT)

    suspend fun set(data: SessionData) {
        writeMutex.withLock {
            sessionTable.saveSession(data)
            _session.value = data
        }
    }

    suspend fun clear() {
        writeMutex.withLock {
            sessionTable.clearSession()
            _session.value = null
        }
    }

    suspend fun restore(): SessionData? {
        return writeMutex.withLock {
            val restored = sessionTable.loadSession()
            _session.value = restored
            restored
        }
    }
}