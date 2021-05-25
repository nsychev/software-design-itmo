package ru.nsychev.sd.gym.domain

import java.time.Instant

data class TicketInfo(
    val ticketId: Long,
    val issueTimestamp: Instant,
    val validity: Long
)
