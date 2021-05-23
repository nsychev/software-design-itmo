package ru.nsychev.sd.reactive.controllers

import io.netty.handler.codec.http.HttpResponseStatus
import io.reactivex.netty.protocol.http.server.HttpServerRequest

fun <T> HttpServerRequest<T>.queryParam(name: String): String {
    return queryParameters.getOrDefault(name, null)
        ?.getOrNull(0)
        ?: throw HttpError(HttpResponseStatus.BAD_REQUEST, "Missing parameter '$name'")
}
