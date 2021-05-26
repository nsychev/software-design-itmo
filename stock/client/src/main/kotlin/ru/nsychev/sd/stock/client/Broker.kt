package ru.nsychev.sd.stock.client

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Broker : KoinComponent {
    private val stockClient by inject<StockClient>()

    val server = embeddedServer(Netty, port = 8081) {
        routing {
            get("/register/{name}") {
                val name = call.parameters["name"]
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid name parameter")

                UserStorage.addUser(name)

                call.respond("OK")
            }

            get("/user/{name}/add-funds/{delta}") {
                val name = call.parameters["name"]
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid name parameter")

                val delta = call.parameters["delta"]?.toDoubleOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid delta parameter")

                if (delta < 0) {
                    return@get call.respond(HttpStatusCode.Forbidden, "Unexpected delta")
                }

                UserStorage.changeFunds(name, delta)

                call.respond("OK")
            }

            get("/user/{name}/stocks") {
                val name = call.parameters["name"]
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid name parameter")

                try {
                    stockClient.getStocks(name)
                        .map { view ->
                            "$${view.ticker}: ${view.quantity} × $${view.price}"
                        }
                        .joinToString("\n")
                        .ifEmpty { "No stocks yet." }
                        .let { call.respond(it) }
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.NotFound, "No such user")
                }
            }

            get("/user/{name}/funds") {
                val name = call.parameters["name"]
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid name parameter")

                try {
                    call.respond("Current funds: ${stockClient.getFunds(name)}")
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.NotFound, "No such user")
                }


            }

            get("/user/{name}/order/{ticker}/{delta}") {
                val name = call.parameters["name"]
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid name parameter")

                val ticker = call.parameters["ticker"]
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid ticker parameter")

                val delta = call.parameters["delta"]?.toIntOrNull()
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Invalid delta parameter")

                try {
                    stockClient.changeStock(name, ticker, delta)
                    call.respond("OK")
                } catch (e: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest, e.message ?: "Unknown error")
                }
            }
        }
    }
}
