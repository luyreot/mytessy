package com.teoryul.mytesy.data.repo

import com.teoryul.mytesy.data.api.ApiClient
import com.teoryul.mytesy.data.model.login.LoginRequest
import com.teoryul.mytesy.data.model.login.LoginResponse
import com.teoryul.mytesy.data.model.oldlogin.OldAppLoginRequest
import com.teoryul.mytesy.data.model.oldlogin.OldAppLoginResponse
import com.teoryul.mytesy.data.model.registrationcheck.CheckRegistrationRequest
import com.teoryul.mytesy.data.model.registrationcheck.CheckRegistrationResponse
import com.teoryul.mytesy.domain.repo.LoginRepository
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class LoginRepositoryImpl : LoginRepository {

    override suspend fun checkRegistration(email: String): CheckRegistrationResponse {
        return withContext(Dispatchers.IO) {
            val request = CheckRegistrationRequest(email = email)

            val response: HttpResponse =
                ApiClient.httpClient.post("https://ad.mytesy.com/rest/app-user-has-registration") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }

            return@withContext response.body()
        }
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        return withContext(Dispatchers.IO) {
            val request = LoginRequest(email = email, password = password)

            val response: HttpResponse =
                ApiClient.httpClient.post("https://ad.mytesy.com/rest/app-user-login") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }

            return@withContext response.body()
        }
    }

    override suspend fun loginOldApp(
        email: String,
        password: String,
        userID: Int?,
        userEmail: String?,
        userPass: String?
    ): OldAppLoginResponse {
        return withContext(Dispatchers.IO) {
            val request = OldAppLoginRequest(
                email = email,
                password = password,
                userID = userID,
                userEmail = email,
                userPass = password
            )

            val response: HttpResponse =
                ApiClient.httpClient.post("https://ad.mytesy.com/rest/old-app-login") {
                    contentType(ContentType.Application.Json)
                    setBody(request)
                }

            return@withContext response.body()
        }
    }
}