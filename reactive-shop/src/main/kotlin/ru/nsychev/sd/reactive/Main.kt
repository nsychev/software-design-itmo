package ru.nsychev.sd.reactive

import io.reactivex.netty.protocol.http.server.HttpServer
import org.koin.core.context.startKoin
import ru.nsychev.sd.reactive.http.Handler

fun main() {
    val handler = startKoin {
        printLogger()
        modules(mainModule)
    }.koin.get<Handler>()

    HttpServer.newServer(8080).start(handler::handle).awaitShutdown()
}

