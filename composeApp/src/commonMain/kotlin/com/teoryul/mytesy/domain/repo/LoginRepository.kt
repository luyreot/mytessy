package com.teoryul.mytesy.domain.repo

import com.teoryul.mytesy.data.model.login.LoginResponse
import com.teoryul.mytesy.data.model.oldlogin.OldAppLoginResponse
import com.teoryul.mytesy.data.model.registrationcheck.CheckRegistrationResponse

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
}