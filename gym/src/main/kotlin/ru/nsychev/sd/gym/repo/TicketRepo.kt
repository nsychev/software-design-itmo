package ru.nsychev.sd.gym.repo

import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.eq
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.nsychev.sd.gym.domain.EventType
import ru.nsychev.sd.gym.domain.TicketInfo
import ru.nsychev.sd.gym.domain.event.ExtendTicketEvent
import ru.nsychev.sd.gym.domain.event.IssueTicketEvent
import java.time.Instant
import kotlin.math.abs
import kotlin.random.Random

class TicketRepo : KoinComponent {
    private val eventRepo by inject<EventRepo>()

    fun issue(instant: Instant, validity: Long): Long {
        val ticketId = abs(Random.nextLong())
        eventRepo.add(IssueTicketEvent(ticketId, instant, validity))
        return ticketId
    }

    fun extend(ticketId: Long, extraValidity: Long) {
        eventRepo.find(
            and(
                eq("type", EventType.ISSUE_TICKET.toString()),
                eq("ticketId", ticketId)
            )
        ).firstOrNull()
            ?: throw IllegalArgumentException("No such ticket.")

        eventRepo.add(ExtendTicketEvent(ticketId, extraValidity))
    }

    fun get(ticketId: Long): TicketInfo {
        val root = eventRepo.find(
            and(
                eq("type", EventType.ISSUE_TICKET.toString()),
                eq("ticketId", ticketId)
            )
        ).firstOrNull() as? IssueTicketEvent
            ?: throw IllegalArgumentException("No such ticket.")

        val validity = eventRepo.find(
            and(
                eq("type", EventType.EXTEND_TICKET.toString()),
                eq("ticketId", ticketId)
            )
        ).fold(root.validity) { oldValidity, event -> when (event) {
            is ExtendTicketEvent -> oldValidity + event.extraValidity
            else -> oldValidity
        } }

        return TicketInfo(ticketId, root.issueTimestamp, validity)
    }
}
