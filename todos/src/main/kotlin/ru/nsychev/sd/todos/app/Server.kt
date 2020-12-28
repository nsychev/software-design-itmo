package ru.nsychev.sd.todos.app

import freemarker.cache.ClassTemplateLoader
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.freemarker.FreeMarker
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.jetty.Jetty
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import ru.nsychev.sd.todos.controller.ItemController
import ru.nsychev.sd.todos.controller.ListController


val server = embeddedServer(Jetty, 8080) {
    install(DefaultHeaders)
    install(CallLogging)

    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
    }

    install(Koin) {
        modules(
            controllerModule,
            serviceModule,
            repositoryModule,
            databaseModule
        )
    }

    val itemController by inject<ItemController>()
    val listController by inject<ListController>()

    routing {
        static("/static/") { resources("static") }

        get("/", listController.all)
        post("/", listController.new)
        get("/lists/{listId}", listController.one)
        get("/lists/{listId}/delete", listController.delete)
        post("/lists/{listId}", itemController.new)
        get("/lists/{listId}/items/{itemId}", itemController.toggleComplete)
    }
}
