package ru.nsychev.sd.feed.api

import com.uchuhimo.konf.Config
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockserver.client.MockServerClient
import org.mockserver.integration.ClientAndServer
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.mockserver.model.MediaType
import ru.nsychev.sd.feed.conf.FeedSpec
import kotlin.test.assertEquals

class VKClientTest {
    private val port = (1024..65535).random()

    private lateinit var mockServer: MockServerClient

    private val config = Config {
        addSpec(FeedSpec)
    }.apply {
        this[FeedSpec.proto] = "http"
        this[FeedSpec.host] = "localhost"
        this[FeedSpec.port] = port
        this[FeedSpec.version] = VERSION
        this[FeedSpec.accessToken] = ACCESS_TOKEN
    }

    @BeforeEach
    fun startServer() {
        mockServer = ClientAndServer.startClientAndServer(port)
    }

    @AfterEach
    fun stopServer() {
        mockServer.close()
    }

    @Test
    fun shouldRunNewsfeedSearch() {
        mockServer.`when`(
            request().apply {
                withMethod("GET")
                withPath("/method/newsfeed.search")
                withQueryStringParameters(mapOf(
                    "v" to listOf(VERSION),
                    "access_token" to listOf(ACCESS_TOKEN),
                    "q" to listOf("#aaa"),
                    "count" to listOf("0"),
                    "start_time" to listOf("789"),
                    "end_time" to listOf("1023")
                ))
            }
        ).respond(
            response().apply {
                withStatusCode(200)
                withContentType(MediaType.APPLICATION_JSON)
                withBody("""
                    {
                        "response": {
                            "items": [],
                            "count": 123,
                            "total_count": 456
                        }
                    }
                """.trimIndent())
            }
        )

        val vkClient = VKClient(config)

        runBlocking {
            assertEquals(
                ListResponse(listOf(), 123, 456, null),
                vkClient.newsfeedSearch("#aaa", 0, 789L, 1023L)
            )
        }
    }

    @Test
    fun shouldThrowOnBadStatusCode() {
        mockServer.`when`(
            request().apply {
                withMethod("GET")
                withPath("/method/newsfeed.search")
            }
        ).respond(
            response().apply {
                withStatusCode(404)
            }
        )

        val vkClient = VKClient(config)

        assertThrows<BadStatusCodeException> {
            runBlocking {
                vkClient.newsfeedSearch("#aaa", 0, 789L, 1023L)
            }
        }
    }

    companion object {
        const val VERSION = "1.2.3"
        const val ACCESS_TOKEN = "test-access-token"
    }
}
