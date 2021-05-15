package ru.nsychev.sd.actorsearch.engines

import akka.actor.UntypedAbstractActor
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import kotlinx.coroutines.runBlocking
import ru.nsychev.sd.actorsearch.messages.SearchRequest
import ru.nsychev.sd.actorsearch.messages.SearchResult
import ru.nsychev.sd.actorsearch.messages.SearchResultResponse

abstract class BaseEngineActor(
    val type: EngineType
) : UntypedAbstractActor() {
    protected val client = HttpClient(CIO) {
        install(JsonFeature)
    }

    override fun onReceive(message: Any?) = when (message) {
        is SearchRequest -> context.parent.tell(search(message.query), self)
        else -> {}
    }

    private fun search(query: String): SearchResultResponse {
        return runBlocking { findBestResults(query) }.map { link ->
            SearchResult(type, link)
        }.let(::SearchResultResponse)
    }

    protected abstract suspend fun findBestResults(query: String): List<String>
}
