package ru.nsychev.sd.todos.domain

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.nsychev.sd.todos.domain.tables.TodoItems
import ru.nsychev.sd.todos.domain.tables.TodoLists

class TodoList(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TodoList>(TodoLists)

    var name by TodoLists.name
    val items by TodoItem referrersOn TodoItems.list
}
