package ru.nsychev.sd.todos.service

import org.koin.core.KoinComponent
import org.koin.core.inject
import ru.nsychev.sd.todos.domain.TodoItem
import ru.nsychev.sd.todos.repository.ItemRepository

class ItemService : KoinComponent {
    private val itemRepository by inject<ItemRepository>()
    private val listService by inject<ListService>()

    fun byListId(id: Int): List<TodoItem> {
        return itemRepository.byListId(id)
    }

    fun create(name: String, listId: Int): TodoItem {
        val list = listService.byId(listId)
        return itemRepository.create(name, list)
    }

    fun byIdAndList(id: Int, listId: Int): TodoItem {
        val list = listService.byId(listId)
        return itemRepository.byIdAndList(id, list)
    }

    fun toggleComplete(item: TodoItem) {
        itemRepository.toggleComplete(item)
    }

    fun deleteByList(listId: Int) {
        itemRepository.deleteByList(listId)
    }
}
