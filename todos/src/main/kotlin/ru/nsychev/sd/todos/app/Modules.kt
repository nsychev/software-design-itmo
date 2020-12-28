package ru.nsychev.sd.todos.app

import org.koin.dsl.module
import ru.nsychev.sd.todos.controller.ItemController
import ru.nsychev.sd.todos.controller.ListController
import ru.nsychev.sd.todos.db.DataSource
import ru.nsychev.sd.todos.repository.ItemRepository
import ru.nsychev.sd.todos.repository.ListRepository
import ru.nsychev.sd.todos.service.ItemService
import ru.nsychev.sd.todos.service.ListService

val controllerModule = module {
    single { ListController() }
    single { ItemController() }
}

val serviceModule = module {
    single { ItemService() }
    single { ListService() }
}

val repositoryModule = module {
    single { ItemRepository() }
    single { ListRepository() }
}

val databaseModule = module {
    single { DataSource() }
}
