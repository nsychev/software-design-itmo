package ru.nsychev.sd.actorsearch.utils

import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.Patterns
import ru.nsychev.sd.actorsearch.master.MasterActor
import ru.nsychev.sd.actorsearch.messages.SearchRequest
import ru.nsychev.sd.actorsearch.messages.SearchResult
import ru.nsychev.sd.actorsearch.messages.SearchResultResponse
import ru.nsychev.sd.actorsearch.timeout
import ru.nsychev.sd.actorsearch.utils.stub.BaseStubServer
import scala.concurrent.Await
import kotlin.system.exitProcess

internal fun withSearchMocks(vararg servers: BaseStubServer, action: () -> Unit) {
    servers.forEach { it.start() }

    try {
        action()
    } finally {
        servers.forEach { it.stop() }
    }
}

internal fun getResults(query: String): List<SearchResult> {
    val system = ActorSystem.create("actorsearch")

    try {
        val actor = system.actorOf(Props.create(MasterActor::class.java), "master")

        val result = Await.result(
            Patterns.ask(actor, SearchRequest(query), timeout),
            timeout.duration()
        )

        if (result !is SearchResultResponse) {
            System.err.println("Unknown message type: ${result.javaClass.simpleName}")
            exitProcess(1)
        }

        return result.data
    } finally {
        system.terminate()
    }
}
