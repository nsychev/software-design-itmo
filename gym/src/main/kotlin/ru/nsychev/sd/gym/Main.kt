package ru.nsychev.sd.gym

import org.koin.core.context.startKoin
import ru.nsychev.sd.gym.web.ManagerApi

fun main() {
    val api = startKoin {
        printLogger()
        modules(serviceModule)
        modules(webModule)
    }.koin.get<ManagerApi>()

    api.server.start(wait = true)
}
