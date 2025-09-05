package com.teoryul.mytesy.data.api

import com.teoryul.mytesy.data.api.plugin.AuthGuardPlugin
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.KotlinxSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

expect fun getHttpClientEngine(): HttpClientEngineFactory<*>

class ApiClient(
    private val json: Json,
    private val authGuardPlugin: AuthGuardPlugin
) {

    val httpClient: HttpClient by lazy {
        HttpClient(getHttpClientEngine()) {
            expectSuccess = true
            install(ContentNegotiation) {
                json(json)
                register(ContentType.Text.Html, KotlinxSerializationConverter(json))
                register(ContentType.Text.Plain, KotlinxSerializationConverter(json))
            }
            install(authGuardPlugin)
        }
    }
}