package com.teoryul.mytesy.data.common

import com.teoryul.mytesy.domain.model.ErrorResult
import com.teoryul.mytesy.domain.model.ErrorResultMapper
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.utils.io.errors.IOException

class ErrorResultMapperImpl : ErrorResultMapper {

    override fun map(t: Throwable): ErrorResult = when (t) {
        is UnauthenticatedException -> ErrorResult.Unauthenticated(t.message.orEmpty())

        is ClientRequestException -> ErrorResult.InvalidInput(
            "${t.response.status.value}: ${this::class.simpleName.orEmpty()}"
        )

        is ServerResponseException -> ErrorResult.ServerError(
            "${t.response.status.value}: ${this::class.simpleName.orEmpty()}"
        )

        is IOException -> ErrorResult.ConnectionError(
            "Connection error: ${this::class.simpleName.orEmpty()}"
        )

        else -> ErrorResult.Unknown(
            "Unknown error: ${this::class.simpleName.orEmpty()}"
        )
    }
}