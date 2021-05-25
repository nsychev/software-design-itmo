package ru.nsychev.sd.gym.service

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.nsychev.sd.gym.repo.TicketRepo
import java.time.Instant

class ManagerService : KoinComponent {
    private val ticketRepo by inject<TicketRepo>()

    fun issue(instant: Instant, validity: Long): Long = ticketRepo.issue(instant, validity)

    fun extend(ticketId: Long, extraValidity: Long) = ticketRepo.extend(ticketId, extraValidity)

    fun get(ticketId: Long) = ticketRepo.get(ticketId)
}
