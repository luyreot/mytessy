package com.teoryul.mytesy.data.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.KotlinxSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

expect fun getHttpClientEngine(): HttpClientEngineFactory<*>

class ApiClient {

    val httpClient: HttpClient by lazy {
        HttpClient(getHttpClientEngine()) {
            install(ContentNegotiation) {
                val json = Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
                json(json)
                register(ContentType.Text.Html, KotlinxSerializationConverter(json))
                register(ContentType.Text.Plain, KotlinxSerializationConverter(json))
            }
        }
    }
}