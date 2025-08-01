package com.teoryul.mytesy.data

import com.teoryul.mytesy.data.login.LoginRequest
import com.teoryul.mytesy.data.login.LoginResponse
import com.teoryul.mytesy.data.oldlogin.OldAppLoginRequest
import com.teoryul.mytesy.data.oldlogin.OldAppLoginResponse
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

class LoginRepository {

    suspend fun login(email: String, password: String): LoginResponse {
        val request = LoginRequest(email = email, password = password)

        val response: HttpResponse =
            ApiClient.httpClient.post("https://ad.mytesy.com/rest/app-user-login") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }

        return response.body()
    }

    suspend fun loginOldApp(
        email: String,
        password: String,
        userID: Int
    ): OldAppLoginResponse {
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

        return response.body()
    }
}