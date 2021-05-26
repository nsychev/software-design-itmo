package ru.nsychev.sd.stock.client.test.core

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.logging.Logging
import org.junit.jupiter.api.BeforeEach
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import ru.nsychev.sd.stock.client.provider.ServerProvider

@Testcontainers
open class BaseTest {
    @Container
    private val container = StockServerContainer()
        .withExposedPorts(8080)
        .withLogConsumer(Slf4jLogConsumer(LoggerFactory.getLogger(BaseTest::class.qualifiedName)))

    @BeforeEach
    fun setClientOptions() {
        val provider: ServerProvider = TestApplication.get()
        (provider as? TestServerProvider)?.setHost(container.host)
        (provider as? TestServerProvider)?.setPort(container.getMappedPort(8080))
    }

    protected val client = HttpClient(CIO) {
        install(Logging)
    }
}
