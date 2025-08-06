package com.teoryul.mytesy.data.db

import com.teoryul.mytesy.db.AppDatabase
import com.teoryul.mytesy.domain.session.SessionData
import com.teoryul.mytesy.domain.session.SessionTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class SessionTableImpl(databaseDriverFactory: DatabaseDriverFactory) : SessionTable {

    private val database = AppDatabase(databaseDriverFactory.createDriver())

    private val queries = database.sessionQueries

    override suspend fun saveSession(data: SessionData) {
        withContext(Dispatchers.IO) {
            queries.insertOrReplace(
                token = data.token,
                acc_session = data.accSession,
                acc_alt = data.accAlt,
                user_id = data.userId,
                email = data.email,
                password = data.password,
                first_name = data.firstName,
                last_name = data.lastName,
                lang = data.lang
            )
        }
    }

    override suspend fun loadSession(): SessionData? {
        return withContext(Dispatchers.IO) {
            queries.selectAll().executeAsOneOrNull()?.let {
                SessionData(
                    token = it.token,
                    accSession = it.acc_session,
                    accAlt = it.acc_alt,
                    userId = it.user_id,
                    email = it.email,
                    password = it.password,
                    firstName = it.first_name,
                    lastName = it.last_name,
                    lang = it.lang
                )
            }
        }
    }

    override suspend fun clearSession() {
        withContext(Dispatchers.IO) {
            queries.deleteAll()
        }
    }
}