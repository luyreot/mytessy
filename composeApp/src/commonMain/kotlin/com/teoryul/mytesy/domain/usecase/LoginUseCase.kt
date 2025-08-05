package com.teoryul.mytesy.domain.usecase

import com.teoryul.mytesy.domain.repo.LoginRepository
import com.teoryul.mytesy.util.AppLogger.e
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginUseCase(
    private val loginRepository: LoginRepository
) {

    suspend operator fun invoke(email: String, password: String): LoginResult {
        return withContext(Dispatchers.Default) {
            try {
                // Account registered check + Old login
                val checkRegistrationResponse = loginRepository.checkRegistration(email)
                if (checkRegistrationResponse.errors != null) {
                    val firstNonNullErrorMessage: String = checkRegistrationResponse.errors.values
                        .firstOrNull { it.isNotEmpty() }
                        ?.firstOrNull()
                        .orEmpty()
                    return@withContext LoginResult.Fail.ResponseError(firstNonNullErrorMessage)
                }
                if (checkRegistrationResponse.success == false) {
                    val oldLoginResponse = loginRepository.loginOldApp(
                        email = email,
                        password = password,
                        userID = null,
                        userEmail = null,
                        userPass = null
                    )
                    if (oldLoginResponse.error != null) {
                        return@withContext when (oldLoginResponse.error) {
                            "1" -> LoginResult.Fail.ResponseError("Wrong E-mail or Password")
                            else -> LoginResult.Fail.ResponseError(oldLoginResponse.error)
                        }
                    }
                    if (oldLoginResponse.acc_alt.isNullOrEmpty() ||
                        oldLoginResponse.acc_session.isNullOrEmpty()
                    ) {
                        return@withContext LoginResult.Fail.ResponseError("Missing parameters")
                    }
                    return@withContext LoginResult.NeedRegistration(
                        alt = oldLoginResponse.acc_alt,
                        session = oldLoginResponse.acc_session
                    )
                }

                // New login
                val loginResponse = loginRepository.login(email, password)
                if (loginResponse.errors != null) {
                    val firstNonNullErrorMessage: String = loginResponse.errors.values
                        .firstOrNull { it.isNotEmpty() }
                        .orEmpty()
                    return@withContext LoginResult.Fail.ResponseError(firstNonNullErrorMessage)
                }
                if (loginResponse.token.isNullOrEmpty() ||
                    loginResponse.userID == null
                ) {
                    return@withContext LoginResult.Fail.ResponseError("Missing parameters")
                }

                // Old login
                val oldLoginResponse = loginRepository.loginOldApp(
                    email = email,
                    password = password,
                    userID = loginResponse.userID,
                    userEmail = email,
                    userPass = password
                )
                if (oldLoginResponse.error != null) {
                    return@withContext when (oldLoginResponse.error) {
                        "1" -> LoginResult.Fail.ResponseError("Wrong E-mail or Password")
                        else -> LoginResult.Fail.ResponseError(oldLoginResponse.error)
                    }
                }
                if (oldLoginResponse.acc_session.isNullOrEmpty()) {
                    return@withContext LoginResult.Fail.ResponseError("Missing parameters")
                }

                return@withContext LoginResult.LoginSuccess(
                    token = loginResponse.token,
                    legacySession = oldLoginResponse.acc_session
                )
            } catch (e: ClientRequestException) {
                e("❌ 4xx: ${e.message}")
                return@withContext LoginResult.Fail.InvalidInput("${e.response.status.value}: ${e::class.simpleName.orEmpty()}")
            } catch (e: ServerResponseException) {
                e("❌ 5xx: ${e.message}")
                return@withContext LoginResult.Fail.ServerError("${e.response.status.value}: ${e::class.simpleName.orEmpty()}")
            } catch (e: IOException) {
                // Handle no internet, timeout, bad input, server crash, and unknowns
                e("❌ Internet connect error: ${e.message}")
                return@withContext LoginResult.Fail.ConnectionError("Connection error: ${e::class.simpleName.orEmpty()}")
            } catch (e: Exception) {
                e("❌ Unknown error: ${e.message}")
                return@withContext LoginResult.Fail.Unknown("Unknown error: ${e::class.simpleName.orEmpty()}")
            }
        }
    }

    sealed class LoginResult() {

        data class LoginSuccess(
            val token: String,
            val legacySession: String
        ) : LoginResult()

        // TODO needs more testing
        data class NeedRegistration(
            val alt: String,
            val session: String
        ) : LoginResult()

        sealed class Fail(val message: String) : LoginResult() {
            data class ResponseError(val msg: String) : Fail(msg)
            data class InvalidInput(val msg: String) : Fail(msg)
            data class ServerError(val msg: String) : Fail(msg)
            data class ConnectionError(val msg: String) : Fail(msg)
            data class Unknown(val msg: String) : Fail(msg)
        }
    }
}