package ru.nsychev.sd.gym.domain.event

import ru.nsychev.sd.gym.domain.EventType

data class ExtendTicketEvent(
    val ticketId: Long,
    val extraValidity: Long
) : Event(EventType.EXTEND_TICKET)
