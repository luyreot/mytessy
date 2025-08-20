package com.teoryul.mytesy.domain.usecase

import com.teoryul.mytesy.data.api.ApiService
import com.teoryul.mytesy.domain.model.ErrorResult
import com.teoryul.mytesy.domain.model.ErrorResultMapper
import com.teoryul.mytesy.domain.model.toErrorResult
import com.teoryul.mytesy.domain.session.SessionData
import com.teoryul.mytesy.domain.session.SessionManager
import com.teoryul.mytesy.infra.AppLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginUseCase(
    private val api: ApiService,
    private val sessionManager: SessionManager,
    private val errorMapper: ErrorResultMapper
) {

    suspend operator fun invoke(email: String, password: String): Result {
        return withContext(Dispatchers.Default) {
            try {
                // Account registered check + Old login
                val checkRegistrationResponse = api.checkRegistration(email)
                if (checkRegistrationResponse.errors != null) {
                    val firstNonNullErrorMessage: String = checkRegistrationResponse.errors.values
                        .firstOrNull { it.isNotEmpty() }
                        ?.firstOrNull()
                        .orEmpty()
                    return@withContext Result.Fail(
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
                            "1" -> Result.Fail(ErrorResult.ResponseError("Wrong E-mail or Password"))
                            else -> Result.Fail(ErrorResult.ResponseError(oldLoginResponse.error))
                        }
                    }
                    if (oldLoginResponse.accAlt.isNullOrEmpty() ||
                        oldLoginResponse.accSession.isNullOrEmpty()
                    ) {
                        return@withContext Result.Fail(ErrorResult.ResponseError("Missing parameters"))
                    }
                    return@withContext Result.AccountNotFound("Account not found")
                }

                // New login
                val loginResponse = api.login(email, password)
                if (loginResponse.errors != null) {
                    val firstNonNullErrorMessage: String = loginResponse.errors.values
                        .firstOrNull { it.isNotEmpty() }
                        .orEmpty()
                    return@withContext Result.Fail(
                        ErrorResult.ResponseError(firstNonNullErrorMessage)
                    )
                }
                if (loginResponse.token.isNullOrEmpty() ||
                    loginResponse.userID == null
                ) {
                    return@withContext Result.Fail(ErrorResult.ResponseError("Missing parameters"))
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
                        "1" -> Result.Fail(ErrorResult.ResponseError("Wrong E-mail or Password"))
                        else -> Result.Fail(ErrorResult.ResponseError(oldLoginResponse.error))
                    }
                }
                if (oldLoginResponse.accSession.isNullOrEmpty() ||
                    oldLoginResponse.accAlt.isNullOrEmpty()
                ) {
                    return@withContext Result.Fail(ErrorResult.ResponseError("Missing session parameters"))
                }

                sessionManager.set(
                    SessionData(
                        token = loginResponse.token,
                        accSession = oldLoginResponse.accSession,
                        accAlt = oldLoginResponse.accAlt,
                        userId = loginResponse.userID,
                        email = loginResponse.email.orEmpty(),
                        password = loginResponse.password.orEmpty(),
                        firstName = loginResponse.firstName.orEmpty(),
                        lastName = loginResponse.lastName.orEmpty(),
                        lang = loginResponse.lang ?: "en"
                    )
                )

                return@withContext Result.Success
            } catch (t: Throwable) {
                val error = t.toErrorResult(errorMapper)
                AppLogger.e(error.message)
                return@withContext Result.Fail(error)
            }
        }
    }

    sealed class Result {

        data object Success : Result()

        data class AccountNotFound(val message: String) : Result()

        data class Fail(val error: ErrorResult) : Result()
    }
}