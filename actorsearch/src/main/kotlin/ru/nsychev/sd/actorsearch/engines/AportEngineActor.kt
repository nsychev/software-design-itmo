package ru.nsychev.sd.actorsearch.engines

import io.ktor.client.request.*

class AportEngineActor : BaseEngineActor(EngineType.APORT) {
    override suspend fun findBestResults(query: String): List<String> {
        return client.get {
            url("http://127.0.0.1:8000/search")
            parameter("query", query)
        }
    }
}
