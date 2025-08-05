package com.teoryul.mytesy.data.db

import com.teoryul.mytesy.db.AppDatabase
import com.teoryul.mytesy.domain.session.SessionData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class SessionDatabase(databaseDriverFactory: DatabaseDriverFactory) {

    private val database = AppDatabase(databaseDriverFactory.createDriver())

    private val queries = database.sessionQueries

    suspend fun saveSession(data: SessionData) {
        withContext(Dispatchers.IO) {
            queries.insertOrReplace(
                token = data.token,
                acc_session = data.accSession,
                acc_alt = data.accAlt,
                user_id = data.userId.toLong(),
                email = data.email,
                first_name = data.firstName,
                last_name = data.lastName,
                lang = data.lang
            )
        }
    }

    suspend fun loadSession(): SessionData? {
        return withContext(Dispatchers.IO) {
            queries.selectAll().executeAsOneOrNull()?.let {
                SessionData(
                    token = it.token,
                    accSession = it.acc_session,
                    accAlt = it.acc_alt,
                    userId = it.user_id.toInt(),
                    email = it.email,
                    firstName = it.first_name,
                    lastName = it.last_name,
                    lang = it.lang
                )
            }
        }
    }

    suspend fun clearSession() {
        withContext(Dispatchers.IO) {
            queries.deleteAll()
        }
    }
}