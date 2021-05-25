package ru.nsychev.sd.gym.domain.event

import ru.nsychev.sd.gym.domain.Direction
import ru.nsychev.sd.gym.domain.EventType
import java.time.Instant

data class VisitEvent(
    val ticketId: Long,
    val direction: Direction,
    val timestamp: Instant,
) : Event(EventType.VISIT_EVENT)
