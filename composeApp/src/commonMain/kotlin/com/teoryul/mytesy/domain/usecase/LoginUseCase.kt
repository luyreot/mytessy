package com.teoryul.mytesy.domain.usecase

import com.teoryul.mytesy.data.api.ApiService
import com.teoryul.mytesy.domain.model.ErrorResult
import com.teoryul.mytesy.domain.model.toErrorResult
import com.teoryul.mytesy.domain.session.SessionData
import com.teoryul.mytesy.domain.session.SessionManager
import com.teoryul.mytesy.util.AppLogger.e
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginUseCase(
    private val api: ApiService,
    private val sessionManager: SessionManager
) {

    suspend operator fun invoke(email: String, password: String): LoginResult {
        return withContext(Dispatchers.Default) {
            try {
                // Account registered check + Old login
                val checkRegistrationResponse = api.checkRegistration(email)
                if (checkRegistrationResponse.errors != null) {
                    val firstNonNullErrorMessage: String = checkRegistrationResponse.errors.values
                        .firstOrNull { it.isNotEmpty() }
                        ?.firstOrNull()
                        .orEmpty()
                    return@withContext LoginResult.Fail(
                        ErrorResult.ResponseError(firstNonNullErrorMessage)
                    )
                }
                if (checkRegistrationResponse.success == false) {
                    val oldLoginResponse = api.loginOldApp(
                        email = email,
                        password = password,
                        userID = null,
                        userEmail = null,
                        userPass = null
                    )
                    if (oldLoginResponse.error != null) {
                        return@withContext when (oldLoginResponse.error) {
                            "1" -> LoginResult.Fail(ErrorResult.ResponseError("Wrong E-mail or Password"))
                            else -> LoginResult.Fail(ErrorResult.ResponseError(oldLoginResponse.error))
                        }
                    }
                    if (oldLoginResponse.acc_alt.isNullOrEmpty() ||
                        oldLoginResponse.acc_session.isNullOrEmpty()
                    ) {
                        return@withContext LoginResult.Fail(ErrorResult.ResponseError("Missing parameters"))
                    }
                    return@withContext LoginResult.AccountNotFound("Account not found")
                }

                // New login
                val loginResponse = api.login(email, password)
                if (loginResponse.errors != null) {
                    val firstNonNullErrorMessage: String = loginResponse.errors.values
                        .firstOrNull { it.isNotEmpty() }
                        .orEmpty()
                    return@withContext LoginResult.Fail(
                        ErrorResult.ResponseError(firstNonNullErrorMessage)
                    )
                }
                if (loginResponse.token.isNullOrEmpty() ||
                    loginResponse.userID == null
                ) {
                    return@withContext LoginResult.Fail(ErrorResult.ResponseError("Missing parameters"))
                }

                // Old login
                val oldLoginResponse = api.loginOldApp(
                    email = email,
                    password = password,
                    userID = loginResponse.userID,
                    userEmail = email,
                    userPass = password
                )
                if (oldLoginResponse.error != null) {
                    return@withContext when (oldLoginResponse.error) {
                        "1" -> LoginResult.Fail(ErrorResult.ResponseError("Wrong E-mail or Password"))
                        else -> LoginResult.Fail(ErrorResult.ResponseError(oldLoginResponse.error))
                    }
                }
                if (oldLoginResponse.acc_session.isNullOrEmpty() ||
                    oldLoginResponse.acc_alt.isNullOrEmpty()
                ) {
                    return@withContext LoginResult.Fail(ErrorResult.ResponseError("Missing parameters"))
                }

                sessionManager.set(
                    SessionData(
                        token = loginResponse.token,
                        accSession = oldLoginResponse.acc_session,
                        accAlt = oldLoginResponse.acc_alt,
                        userId = loginResponse.userID.toLong(),
                        email = loginResponse.email.orEmpty(),
                        password = loginResponse.password.orEmpty(),
                        firstName = loginResponse.firstName.orEmpty(),
                        lastName = loginResponse.lastName.orEmpty(),
                        lang = loginResponse.lang ?: "en"
                    )
                )

                return@withContext LoginResult.Success
            } catch (t: Throwable) {
                val error = t.toErrorResult()
                e(error.message)
                return@withContext LoginResult.Fail(error)
            }
        }
    }

    sealed class LoginResult() {

        data object Success : LoginResult()

        data class AccountNotFound(val message: String) : LoginResult()

        data class Fail(val error: ErrorResult) : LoginResult()
    }
}