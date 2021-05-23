package ru.nsychev.sd.reactive

import com.mongodb.rx.client.MongoClients
import org.koin.dsl.module
import ru.nsychev.sd.reactive.controllers.AddController
import ru.nsychev.sd.reactive.controllers.ListController
import ru.nsychev.sd.reactive.controllers.RegistrationController
import ru.nsychev.sd.reactive.dao.ItemDao
import ru.nsychev.sd.reactive.dao.UserDao
import ru.nsychev.sd.reactive.http.Handler
import ru.nsychev.sd.reactive.service.ItemService
import ru.nsychev.sd.reactive.service.UserService

val mainModule = module {
    single { MongoClients.create("mongodb://localhost:27017") }
    single { ItemDao() }
    single { UserDao() }
    single { ItemService() }
    single { UserService() }
    single { RegistrationController() }
    single { AddController() }
    single { ListController() }
    single { Handler() }
}
