package ru.nsychev.sd.gym.web

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.http.toHttpDateString
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.nsychev.sd.gym.service.ManagerService
import java.time.Instant


class ManagerApi : KoinComponent {
    private val managerService by inject<ManagerService>()

    val server = embeddedServer(Netty, port = 8080) {
        routing {
            get("/ticket/{ticketId}") {
                val ticketId = call.longParameter("ticketId") ?: return@get

                val ticket = try {
                    managerService.get(ticketId)
                } catch (ignored: IllegalArgumentException) {
                    print(ignored)
                    return@get call.respond(HttpStatusCode.NotFound, "No such ticket")
                }

                call.respond("""
                    Ticket: ${ticket.ticketId}
                    Issued: ${ticket.issueTimestamp.toHttpDateString()}
                    Valid for ${ticket.validity} days
                    """.trimIndent())
            }

            get("/issue/{days}") {
                val days = call.longParameter("days") ?: return@get

                val ticketId = managerService.issue(Instant.now(), days)

                call.respondRedirect("/ticket/${ticketId}", permanent = false)
            }

            get("/ticket/{ticketId}/prolong/{days}") {
                val ticketId = call.longParameter("ticketId") ?: return@get
                val days = call.longParameter("days") ?: return@get

                try {
                    managerService.extend(ticketId, days)
                } catch (ignored: IllegalArgumentException) {
                    return@get call.respond(HttpStatusCode.NotFound, "No such ticket")
                }

                call.respondRedirect("/ticket/${ticketId}", permanent = false)
            }
        }
    }
}

suspend fun ApplicationCall.longParameter(name: String): Long? {
    val value = parameters[name]?.toLongOrNull()

    if (value == null) {
        respond(HttpStatusCode.BadRequest, "Expected long parameter $name")
    }

    return value
}
