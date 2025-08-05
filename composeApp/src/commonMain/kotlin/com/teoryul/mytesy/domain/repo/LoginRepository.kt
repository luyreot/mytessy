package com.teoryul.mytesy.domain.repo

import com.teoryul.mytesy.data.api.model.login.LoginResponse
import com.teoryul.mytesy.data.api.model.oldlogin.OldAppLoginResponse
import com.teoryul.mytesy.data.api.model.registrationcheck.CheckRegistrationResponse
import com.teoryul.mytesy.domain.session.SessionData

interface LoginRepository {

    suspend fun checkRegistration(
        email: String
    ): CheckRegistrationResponse

    suspend fun login(
        email: String,
        password: String
    ): LoginResponse

    suspend fun loginOldApp(
        email: String,
        password: String,
        userID: Int?,
        userEmail: String?,
        userPass: String?
    ): OldAppLoginResponse

    suspend fun saveSession(data: SessionData)
}