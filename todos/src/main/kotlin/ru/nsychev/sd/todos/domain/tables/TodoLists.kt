package ru.nsychev.sd.todos.domain.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object TodoLists : IntIdTable() {
    val name = varchar("name", 50)
}
