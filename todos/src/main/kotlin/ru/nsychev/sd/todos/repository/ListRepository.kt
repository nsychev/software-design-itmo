package ru.nsychev.sd.todos.repository

import org.koin.core.KoinComponent
import org.koin.core.inject
import ru.nsychev.sd.todos.db.DataSource
import ru.nsychev.sd.todos.domain.TodoList
import ru.nsychev.sd.todos.error.ListNotFoundError

class ListRepository : KoinComponent {
    private val dataSource by inject<DataSource>()

    fun all(): List<TodoList> = dataSource.query {
        TodoList.all().toList()
    }

    fun byId(id: Int): TodoList = dataSource.query {
        TodoList.findById(id) ?: throw ListNotFoundError(id)
    }

    fun create(name: String) = dataSource.query {
        TodoList.new {
            this.name = name
        }
    }

    fun delete(list: TodoList) = dataSource.query {
        list.delete()
    }
}
