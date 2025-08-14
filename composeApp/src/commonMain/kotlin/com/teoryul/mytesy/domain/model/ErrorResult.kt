package com.teoryul.mytesy.domain.model

import com.teoryul.mytesy.domain.session.UnauthenticatedException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.utils.io.errors.IOException

sealed class ErrorResult(open val message: String) {

    // 200 OK but bad domain state
    data class ResponseError(override val message: String) : ErrorResult(message)

    // UnauthenticatedError exception
    data class Unauthenticated(override val message: String) : ErrorResult(message)

    // 4xx
    data class InvalidInput(override val message: String) : ErrorResult(message)

    // 5xx
    data class ServerError(override val message: String) : ErrorResult(message)

    // offline / timeout / DNS
    data class ConnectionError(override val message: String) : ErrorResult(message)

    data class Unknown(override val message: String) : ErrorResult(message)
}

fun Throwable.toErrorResult(): ErrorResult = when (this) {
    is UnauthenticatedException -> ErrorResult.Unauthenticated(message.orEmpty())

    is ClientRequestException -> ErrorResult.InvalidInput(
        "${response.status.value}: ${this::class.simpleName.orEmpty()}"
    )

    is ServerResponseException -> ErrorResult.ServerError(
        "${response.status.value}: ${this::class.simpleName.orEmpty()}"
    )

    is IOException -> ErrorResult.ConnectionError(
        "Connection error: ${this::class.simpleName.orEmpty()}"
    )

    else -> ErrorResult.Unknown(
        "Unknown error: ${this::class.simpleName.orEmpty()}"
    )
}