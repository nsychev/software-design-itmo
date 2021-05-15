package ru.nsychev.sd.actorsearch.engines

import io.ktor.client.request.*

class SputnikEngineActor : BaseEngineActor(EngineType.SPUTNIK) {
    override suspend fun findBestResults(query: String): List<String> {
        return client.get {
            url("http://127.0.0.1:10000/search")
            parameter("query", query)
        }
    }
}
