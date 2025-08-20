package com.teoryul.mytesy.data.api

import com.teoryul.mytesy.data.api.model.appliance.OldAppDevicesRequest
import com.teoryul.mytesy.data.api.model.appliance.OldAppDevicesResponse
import com.teoryul.mytesy.data.api.model.login.LoginRequest
import com.teoryul.mytesy.data.api.model.login.LoginResponse
import com.teoryul.mytesy.data.api.model.oldlogin.OldAppLoginRequest
import com.teoryul.mytesy.data.api.model.oldlogin.OldAppLoginResponse
import com.teoryul.mytesy.data.api.model.registrationcheck.CheckRegistrationRequest
import com.teoryul.mytesy.data.api.model.registrationcheck.CheckRegistrationResponse
import com.teoryul.mytesy.data.common.UnauthenticatedException
import com.teoryul.mytesy.domain.session.SessionProvider
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class ApiService(
    private val client: ApiClient,
    private val sessionProvider: SessionProvider
) {

    suspend fun checkRegistration(
        email: String
    ): CheckRegistrationResponse = withContext(Dispatchers.IO) {
        val request = CheckRegistrationRequest(email = email)

        client.httpClient.post("https://ad.mytesy.com/rest/app-user-has-registration") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun login(
        email: String,
        password: String
    ): LoginResponse = withContext(Dispatchers.IO) {
        val request = LoginRequest(email = email, password = password)

        client.httpClient.post("https://ad.mytesy.com/rest/app-user-login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun loginOldApp(
        email: String,
        password: String,
        userID: Long?,
        userEmail: String? = email,
        userPass: String? = password
    ): OldAppLoginResponse = withContext(Dispatchers.IO) {
        val request = OldAppLoginRequest(
            email = email,
            password = password,
            userID = userID,
            userEmail = userEmail,
            userPass = userPass
        )

        client.httpClient.post("https://ad.mytesy.com/rest/old-app-login") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun getOldAppDevices(
    ): OldAppDevicesResponse = withContext(Dispatchers.IO) {
        val session = sessionProvider.require() ?: throw UnauthenticatedException()

        val request = OldAppDevicesRequest(
            alt = session.accAlt,
            currentSession = null,
            phpSessId = session.accSession,
            lang = session.lang,
            lastLoginUsername = session.email,
            userEmail = session.email,
            userID = session.userId,
            userPass = session.password
        )

        client.httpClient.post("https://ad.mytesy.com/rest/old-app-devices") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}