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
import ru.nsychev.sd.todos.domain.TodoList
import ru.nsychev.sd.todos.error.ListNotFoundError
import ru.nsychev.sd.todos.service.ItemService
import ru.nsychev.sd.todos.service.ListService

class ListController : KoinComponent {
    private val itemService by inject<ItemService>()
    private val listService by inject<ListService>()

    val all: PipelineInterceptor<Unit, ApplicationCall> = {
        val lists = listService.all()
        call.respondTemplate(
            "lists.ftl",
            mapOf("lists" to lists)
        )
    }

    val new: PipelineInterceptor<Unit, ApplicationCall> = new@{
        val postParameters = call.receiveParameters()
        val name = postParameters["name"] ?: return@new call.respond(HttpStatusCode.BadRequest)

        listService.create(name)

        call.respondRedirect("/")
    }

    val one: PipelineInterceptor<Unit, ApplicationCall> = one@{
        val list = call.getList() ?: return@one

        val items = itemService.byListId(list.id.value)

        call.respondTemplate(
            "list.ftl",
            mapOf("list" to list, "items" to items)
        )
    }

    val delete: PipelineInterceptor<Unit, ApplicationCall> = delete@{
        val list = call.getList() ?: return@delete

        listService.delete(list)

        call.respondRedirect("/")
    }

    private suspend fun ApplicationCall.getList(): TodoList? {
        val id = parameters["listId"]?.toIntOrNull(10)

        if (id == null) {
            respond(HttpStatusCode.BadRequest)
            return null
        }

        return try {
            listService.byId(id)
        } catch (exc: ListNotFoundError) {
            respondTemplate("noList.ftl", mapOf("id" to exc.id))
            null
        }
    }
}
