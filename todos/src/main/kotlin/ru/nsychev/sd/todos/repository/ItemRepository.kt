package ru.nsychev.sd.todos.repository

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.load
import org.koin.core.KoinComponent
import org.koin.core.inject
import ru.nsychev.sd.todos.db.DataSource
import ru.nsychev.sd.todos.domain.TodoItem
import ru.nsychev.sd.todos.domain.TodoList
import ru.nsychev.sd.todos.domain.tables.TodoItems
import ru.nsychev.sd.todos.domain.tables.TodoLists
import ru.nsychev.sd.todos.error.ItemNotFoundError


class ItemRepository : KoinComponent {
    private val dataSource by inject<DataSource>()

    fun byListId(id: Int): List<TodoItem> = dataSource.query {
        TodoItem.find {
            TodoItems.list eq EntityID(id, TodoLists)
        }.toList()
    }

    fun create(name: String, list: TodoList): TodoItem = dataSource.query {
        TodoItem.new {
            this.name = name
            this.list = list
        }
    }

    fun byIdAndList(id: Int, list: TodoList): TodoItem = dataSource.query {
        TodoItem.findById(id)
            ?.load(TodoItem::list)
            ?.apply {
                if (this.list.id != list.id) throw ItemNotFoundError(id)
            }
            ?: throw ItemNotFoundError(id)
    }

    fun toggleComplete(item: TodoItem) = dataSource.query {
        item.isComplete = !item.isComplete
    }

    fun deleteByList(listId: Int) = dataSource.query {
        TodoItem.find {
            TodoItems.list eq EntityID(listId, TodoLists)
        }.map(TodoItem::delete)
    }
}
