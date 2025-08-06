package com.teoryul.mytesy.data.api

import com.teoryul.mytesy.data.api.model.appliance.OldAppDevicesRequest
import com.teoryul.mytesy.data.api.model.appliance.OldAppDevicesResponse
import com.teoryul.mytesy.data.api.model.login.LoginRequest
import com.teoryul.mytesy.data.api.model.login.LoginResponse
import com.teoryul.mytesy.data.api.model.oldlogin.OldAppLoginRequest
import com.teoryul.mytesy.data.api.model.oldlogin.OldAppLoginResponse
import com.teoryul.mytesy.data.api.model.registrationcheck.CheckRegistrationRequest
import com.teoryul.mytesy.data.api.model.registrationcheck.CheckRegistrationResponse
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class ApiService(
    private val client: ApiClient
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
        userID: Int?,
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
        alt: String,
        currentSession: String?,
        phpSessId: String,
        lang: String,
        lastLoginUsername: String,
        userEmail: String,
        userID: Long,
        userPass: String
    ): OldAppDevicesResponse = withContext(Dispatchers.IO) {
        val request = OldAppDevicesRequest(
            ALT = alt,
            CURRENT_SESSION = currentSession,
            PHPSESSID = phpSessId,
            lang = lang,
            last_login_username = lastLoginUsername,
            userEmail = userEmail,
            userID = userID,
            userPass = userPass
        )

        client.httpClient.post("https://ad.mytesy.com/rest/old-app-devices") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }
}