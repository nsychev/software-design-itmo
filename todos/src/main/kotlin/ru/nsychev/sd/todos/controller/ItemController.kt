package ru.nsychev.sd.todos.controller

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.freemarker.respondTemplate
import io.ktor.http.HttpStatusCode
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.util.pipeline.PipelineInterceptor
import org.koin.core.KoinComponent
import org.koin.core.inject
import ru.nsychev.sd.todos.error.ItemNotFoundError
import ru.nsychev.sd.todos.error.ListNotFoundError
import ru.nsychev.sd.todos.service.ItemService

class ItemController : KoinComponent {
    private val itemService by inject<ItemService>()

    val new: PipelineInterceptor<Unit, ApplicationCall> = new@{
        val listId = call.parameters["listId"]?.toIntOrNull(10)
            ?: return@new call.respond(HttpStatusCode.BadRequest)

        val postParameters = call.receiveParameters()
        val name = postParameters["name"] ?: return@new call.respond(HttpStatusCode.BadRequest)

        try {
            itemService.create(name, listId)
        } catch (exc: ListNotFoundError) {
            return@new call.respondTemplate("noList.ftl", mapOf("id" to exc.id))
        }

        call.respondRedirect("/lists/${listId}")
    }

    val toggleComplete: PipelineInterceptor<Unit, ApplicationCall> = toggleComplete@{
        val listId = call.parameters["listId"]?.toIntOrNull(10)
            ?: return@toggleComplete call.respond(HttpStatusCode.BadRequest)

        val itemId = call.parameters["itemId"]?.toIntOrNull(10)
            ?: return@toggleComplete call.respond(HttpStatusCode.BadRequest)

        val item = try {
            itemService.byIdAndList(itemId, listId)
        } catch (exc: ItemNotFoundError) {
            return@toggleComplete call.respondTemplate("noItem.ftl", mapOf("id" to exc.id))
        }  catch (exc: ListNotFoundError) {
            return@toggleComplete call.respondTemplate("noList.ftl", mapOf("id" to exc.id))
        }

        itemService.toggleComplete(item)

        call.respondRedirect("/lists/${listId}")
    }
}
