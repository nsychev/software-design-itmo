package ru.nsychev.sd.actorsearch.master

import akka.actor.ActorRef
import akka.actor.Props
import akka.actor.ReceiveTimeout
import akka.actor.UntypedAbstractActor
import ru.nsychev.sd.actorsearch.engines.EngineType
import ru.nsychev.sd.actorsearch.engines.resolve
import ru.nsychev.sd.actorsearch.messages.SearchRequest
import ru.nsychev.sd.actorsearch.messages.SearchResult
import ru.nsychev.sd.actorsearch.messages.SearchResultResponse
import java.time.Duration

class MasterActor : UntypedAbstractActor() {
    private val results = mutableListOf<SearchResult>()
    private var counter = 0
    private lateinit var senderRef: ActorRef

    override fun onReceive(message: Any?) = when (message) {
        is SearchRequest -> {
            this.senderRef = sender()
            scheduleRequest(message)
        }
        is SearchResultResponse -> appendResult(message)
        is ReceiveTimeout -> stopProcessing()
        else -> {}
    }

    private fun scheduleRequest(request: SearchRequest) {
        context.receiveTimeout = Duration.ofMillis(1000)
        counter = EngineType.values().size
        EngineType.values().forEach { type ->
            context.actorOf(Props.create(type.resolve()), type.toString())
                .tell(request, self)
        }
    }

    private fun appendResult(response: SearchResultResponse) {
        results.addAll(response.data)

        counter -= 1

        if (counter == 0) {
            stopProcessing()
        }
    }

    private fun stopProcessing() {
        // context.parent.tell(SearchResultResponse(results), self)
        senderRef.tell(SearchResultResponse(results), self)
        context.stop(self)
    }
}
