package ru.nsychev.sd.gym

import com.mongodb.client.MongoClients
import org.koin.dsl.module
import ru.nsychev.sd.gym.repo.EventRepo
import ru.nsychev.sd.gym.repo.TicketRepo
import ru.nsychev.sd.gym.repo.VisitRepo
import ru.nsychev.sd.gym.service.ManagerService
import ru.nsychev.sd.gym.service.ReportService
import ru.nsychev.sd.gym.service.TurnstileService
import ru.nsychev.sd.gym.web.ManagerApi

val serviceModule = module {
    single { MongoClients.create("mongodb://localhost:27017").getDatabase("db") }
    single { EventRepo() }
    single { TicketRepo() }
    single { VisitRepo() }
    single { ManagerService() }
    single { ReportService() }
    single { TurnstileService() }
}

val webModule = module {
    single { ManagerApi() }
}
