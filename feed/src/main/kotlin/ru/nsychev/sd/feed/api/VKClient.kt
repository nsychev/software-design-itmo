package ru.nsychev.sd.feed.api

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.uchuhimo.konf.Config
import io.ktor.client.HttpClient
import io.ktor.client.call.receive
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.HttpTimeout
import io.ktor.client.features.HttpRequestTimeoutException
import io.ktor.client.features.json.JacksonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readText
import io.ktor.http.HttpStatusCode
import ru.nsychev.sd.feed.conf.FeedSpec
import java.io.Closeable
import java.net.http.HttpConnectTimeoutException

open class VKClient(
    private val config: Config
) : Closeable {
    private val httpClient = HttpClient(CIO) {
        expectSuccess = false

        install(HttpTimeout) {
            requestTimeoutMillis = 1000
        }

        install(JsonFeature) {
            serializer = JacksonSerializer() {
                propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
            }
        }
    }

    private suspend inline fun <reified T> request(method: String, args: Map<String, Any>): T {
        val response: HttpResponse = try {
            httpClient.request {
                url(
                    config[FeedSpec.proto],
                    config[FeedSpec.host],
                    config[FeedSpec.port],
                    "/method/$method"
                )
                args.forEach(this::parameter)
                parameter("v", config[FeedSpec.version])
                parameter("access_token", config[FeedSpec.accessToken])
            }
        } catch (exc: Exception) {
            when (exc) {
                is HttpConnectTimeoutException,
                is HttpRequestTimeoutException -> throw TimeoutException(exc)
                else -> throw RequestFailedException(exc)
            }
        }

        if (response.status != HttpStatusCode.OK) {
            throw BadStatusCodeException(response.status.value, response.readText())
        }

        try {
            return response.receive()
        } catch (exc: MissingKotlinParameterException) {
            throw UnexpectedKeyException(exc.parameter.name ?: "[untitled]")
        }
    }

    open suspend fun newsfeedSearch(
        q: String,
        count: Int,
        startTime: Long,
        endTime: Long
    ): ListResponse<JsonNode> {
        return request<Wrapper<ListResponse<JsonNode>>>("newsfeed.search", mapOf(
            "q" to q,
            "count" to count,
            "start_time" to startTime,
            "end_time" to endTime
        )).response
    }

    override fun close() {
        httpClient.close()
    }
}
