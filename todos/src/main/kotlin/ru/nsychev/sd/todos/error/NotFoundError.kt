package ru.nsychev.sd.todos.error

open class NotFoundError(className: String, val id: Int): Exception("No $className found by ID = $id")

class ItemNotFoundError(id: Int) : NotFoundError("Item", id)

class ListNotFoundError(id: Int) : NotFoundError("List", id)
