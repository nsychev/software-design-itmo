package ru.nsychev.sd.gym.domain.event

import org.litote.kmongo.Data
import ru.nsychev.sd.gym.domain.EventType

@Data
abstract class Event(
    val type: EventType
)
