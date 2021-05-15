package ru.nsychev.sd.actorsearch.messages

import ru.nsychev.sd.actorsearch.engines.EngineType

data class SearchResult(
    val type: EngineType,
    val link: String
)
