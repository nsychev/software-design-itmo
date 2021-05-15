package ru.nsychev.sd.actorsearch.utils.stub

import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.serialization.json

import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.delay

abstract class BaseStubServer(port: Int) {
    protected val server = embeddedServer(Netty, port = port) {
        install(Routing)
        install(ContentNegotiation) {
            json()
        }
        routing {
            get("/search") {
                delay(timeout)
                call.respond(search())
            }
        }
    }

    fun start() = server.start()

    fun stop() = server.stop(0, 1000)

    protected open val timeout: Long = 0

    protected abstract fun search(): List<String>
}
