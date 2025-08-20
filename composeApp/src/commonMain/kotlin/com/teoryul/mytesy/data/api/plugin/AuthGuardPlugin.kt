package com.teoryul.mytesy.data.api.plugin

import com.teoryul.mytesy.data.api.model.RequiresSession
import com.teoryul.mytesy.data.common.UnauthenticatedException
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.HttpSendPipeline
import io.ktor.client.statement.HttpReceivePipeline
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.util.AttributeKey
import io.ktor.util.date.GMTDate
import io.ktor.util.toByteArray
import io.ktor.utils.io.ByteReadChannel
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

/**
 * AuthGuardPlugin:
 *  - on SEND: tag requests whose body implements RequiresSession
 *  - on RECEIVE: if tagged and 2xx, peek response text (DoubleReceive must be installed),
 *                detect "invalid session" JSON, and throw UnauthenticatedException.
 */
class AuthGuardPlugin(
    private val json: Json
) : HttpClientPlugin<Unit, AuthGuardPlugin> {

    override val key: AttributeKey<AuthGuardPlugin> = AttributeKey(this::class.simpleName.orEmpty())

    private val attrRequiresSession = AttributeKey<Boolean>("auth.requiresSession")

    override fun prepare(block: Unit.() -> Unit): AuthGuardPlugin = this

    override fun install(plugin: AuthGuardPlugin, scope: HttpClient) {
        // Tag outgoing requests whose body is RequiresSession
        scope.sendPipeline.intercept(HttpSendPipeline.Monitoring) {
            if (context.body is RequiresSession) {
                context.attributes.put(attrRequiresSession, true)
            }
            proceed()
        }

        // Intercept RECEIVE, manually buffer body, check, re-create
        scope.receivePipeline.intercept(HttpReceivePipeline.After) {
            val resp = subject

            val requiresSession =
                resp.call.request.attributes.getOrNull(attrRequiresSession) == true
            if (!requiresSession) {
                proceed()
                return@intercept
            }

            val bytes = resp.bodyAsChannel().toByteArray()
            val text = bytes.decodeToString()
            if (isInvalidSessionBody(text)) throw UnauthenticatedException()

            // Reconstruct response so .body<T>() downstream still works
            proceedWith(resp.call.response.copyWithContent(bytes))
        }
    }

    private fun isInvalidSessionBody(body: String): Boolean = runCatching {
        val root = json.parseToJsonElement(body) as? JsonObject ?: return@runCatching false
        val login = root["login"] as? JsonObject ?: return@runCatching false
        val err = (login["error"] as? JsonPrimitive)?.content
        val msg = (login["msg"] as? JsonPrimitive)?.content
        err == "1" || (msg?.contains("invalid", ignoreCase = true) == true)
    }.getOrDefault(false)

    @OptIn(io.ktor.util.InternalAPI::class)
    private fun HttpResponse.copyWithContent(bytes: ByteArray): HttpResponse {
        val source = ByteReadChannel(bytes)
        return object : HttpResponse() {
            override val call = this@copyWithContent.call
            override val status = this@copyWithContent.status
            override val version = this@copyWithContent.version
            override val requestTime = this@copyWithContent.requestTime
            override val responseTime = GMTDate()
            override val headers = this@copyWithContent.headers
            override val coroutineContext = this@copyWithContent.coroutineContext
            override val content: ByteReadChannel = source
        }
    }
}