package ru.nsychev.sd.stock.client

import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.nsychev.sd.stock.client.provider.DefaultServerProvider
import ru.nsychev.sd.stock.client.provider.ServerProvider

fun main() {
    val application = startKoin {
        modules(
            module {
                single { DefaultServerProvider() } bind ServerProvider::class
            },
            appModule
        )
    }

    application.koin.get<Broker>().server.start(wait = true)
}
