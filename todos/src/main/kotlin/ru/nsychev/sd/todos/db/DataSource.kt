package ru.nsychev.sd.todos.db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.KoinComponent
import ru.nsychev.sd.todos.domain.tables.TodoItems
import ru.nsychev.sd.todos.domain.tables.TodoLists

class DataSource : KoinComponent {
    init {
        connect()
        createSchema()
    }

    private fun connect() {
        Database.connect("jdbc:sqlite:./db.sqlite", driver="org.sqlite.JDBC")
    }

    private fun createSchema() {
        transaction {
            SchemaUtils.create(TodoItems, TodoLists)
        }
    }

    fun <T> query(block: () -> T): T = transaction { block() }
}
