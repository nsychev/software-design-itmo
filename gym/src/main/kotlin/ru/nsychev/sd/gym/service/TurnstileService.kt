package ru.nsychev.sd.gym.service

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.nsychev.sd.gym.domain.Direction
import ru.nsychev.sd.gym.repo.TicketRepo
import ru.nsychev.sd.gym.repo.VisitRepo
import java.time.Duration
import java.time.Instant

class TurnstileService : KoinComponent {
    private val ticketRepo by inject<TicketRepo>()
    private val visitRepo by inject<VisitRepo>()

    fun pass(ticketId: Long, direction: Direction, timestamp: Instant) {
        val ticket = ticketRepo.get(ticketId)
        if (ticket.issueTimestamp + Duration.ofDays(ticket.validity) < timestamp) {
            throw IllegalArgumentException("Ticket has expired")
        }

        visitRepo.mark(ticketId, direction, timestamp)
    }
}
