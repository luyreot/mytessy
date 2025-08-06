package com.teoryul.mytesy.data.repo

import com.teoryul.mytesy.data.api.ApiService
import com.teoryul.mytesy.data.api.model.login.LoginResponse
import com.teoryul.mytesy.data.api.model.oldlogin.OldAppLoginResponse
import com.teoryul.mytesy.data.api.model.registrationcheck.CheckRegistrationResponse
import com.teoryul.mytesy.domain.repo.LoginRepository
import com.teoryul.mytesy.domain.session.SessionData
import com.teoryul.mytesy.domain.session.SessionTable

class LoginRepositoryImpl(
    private val api: ApiService,
    private val sessionTable: SessionTable
) : LoginRepository {

    override suspend fun checkRegistration(email: String): CheckRegistrationResponse {
        return api.checkRegistration(email)
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        return api.login(email, password)
    }

    override suspend fun loginOldApp(
        email: String,
        password: String,
        userID: Int?,
        userEmail: String?,
        userPass: String?
    ): OldAppLoginResponse {
        return api.loginOldApp(email, password, userID, userEmail, userPass)
    }

    override suspend fun saveSession(
        data: SessionData
    ) {
        sessionTable.saveSession(data)
    }
}