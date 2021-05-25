package ru.nsychev.sd.gym.test.core

import org.koin.core.context.startKoin
import ru.nsychev.sd.gym.repo.EventRepo
import ru.nsychev.sd.gym.serviceModule

object TestApplication {
    val app = startKoin {
        modules(serviceModule)
    }.koin

    inline fun <reified T : Any> get() = app.get<T>()

    fun clean() {
        get<EventRepo>().clean()
    }
}
