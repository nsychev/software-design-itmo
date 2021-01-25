package ru.nsychev.sd.feed.api

data class Wrapper<T>(val response: T)

data class ListResponse<T>(
    val items: List<T>,
    val count: Int,
    val totalCount: Int,
    val suggestedQueries: Any?
)
