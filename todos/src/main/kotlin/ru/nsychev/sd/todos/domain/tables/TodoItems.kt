package ru.nsychev.sd.todos.domain.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object TodoItems : IntIdTable() {
    val name = varchar("name", 50)
    val isComplete = bool("is_complete").default(false)
    val list = reference("list", TodoLists)
}
