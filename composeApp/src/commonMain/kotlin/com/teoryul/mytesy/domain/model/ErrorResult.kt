package com.teoryul.mytesy.domain.model

interface ErrorResultMapper {
    fun map(t: Throwable): ErrorResult
}

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

fun Throwable.toErrorResult(mapper: ErrorResultMapper): ErrorResult = mapper.map(this)