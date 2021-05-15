package ru.nsychev.sd.actorsearch.engines

import io.ktor.client.request.*

class RamblerEngineActor : BaseEngineActor(EngineType.RAMBLER) {
    override suspend fun findBestResults(query: String): List<String> {
        return client.get {
            url("http://127.0.0.1:9000/search")
            parameter("query", query)
        }
    }
}
