package ru.nsychev.sd.todos.service

import org.koin.core.KoinComponent
import org.koin.core.inject
import ru.nsychev.sd.todos.domain.TodoList
import ru.nsychev.sd.todos.repository.ListRepository

class ListService : KoinComponent {
    private val itemService by inject<ItemService>()
    private val listRepository by inject<ListRepository>()

    fun all(): List<TodoList> {
        return listRepository.all()
    }

    fun create(name: String): TodoList {
        return listRepository.create(name)
    }

    fun byId(id: Int): TodoList {
        return listRepository.byId(id)
    }

    fun delete(list: TodoList) {
        itemService.deleteByList(list.id.value)
        listRepository.delete(list)
    }
}
