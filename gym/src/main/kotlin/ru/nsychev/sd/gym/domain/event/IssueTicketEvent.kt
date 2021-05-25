package ru.nsychev.sd.gym.domain.event

import ru.nsychev.sd.gym.domain.EventType
import java.time.Instant

data class IssueTicketEvent(
    val ticketId: Long,
    val issueTimestamp: Instant,
    val validity: Long
) : Event(EventType.ISSUE_TICKET)
