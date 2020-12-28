package ru.nsychev.sd.todos.domain

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import ru.nsychev.sd.todos.domain.tables.TodoItems

class TodoItem(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TodoItem>(TodoItems)

    var name by TodoItems.name
    var isComplete by TodoItems.isComplete
    var list by TodoList referencedOn TodoItems.list
}
