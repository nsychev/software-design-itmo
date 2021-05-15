package ru.nsychev.sd.actorsearch

import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.Patterns
import akka.util.Timeout
import ru.nsychev.sd.actorsearch.master.MasterActor
import ru.nsychev.sd.actorsearch.messages.SearchRequest
import ru.nsychev.sd.actorsearch.messages.SearchResultResponse
import ru.nsychev.sd.actorsearch.utils.stub.FakerStubServer
import scala.concurrent.Await
import java.time.Duration
import kotlin.system.exitProcess


val timeout: Timeout = Timeout.create(Duration.ofSeconds(5))

fun main() {
    val servers = listOf(8000, 9000, 10000).map { port ->
        FakerStubServer(port).apply { start() }
    }

    val system = ActorSystem.create("actorsearch")

    while (true) {
        println("Enter your query:")
        val query = readLine() ?: break

        println("Searching for '$query'...")

        val actor = system.actorOf(Props.create(MasterActor::class.java), "master")

        val result = Await.result(
            Patterns.ask(actor, SearchRequest(query), timeout),
            timeout.duration()
        )

        if (result is SearchResultResponse) {
            println("Results:")
            result.data.forEach { row ->
                println("${row.type.name}: ${row.link}")
            }
        } else {
            System.err.println("Unknown message type: ${result.javaClass.simpleName}")
            exitProcess(1)
        }
    }

    servers.forEach { it.stop() }
}
