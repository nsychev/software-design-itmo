package ru.nsychev.sd.gym.repo

import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.Filters.empty
import org.bson.conversions.Bson
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.nsychev.sd.gym.domain.Direction
import ru.nsychev.sd.gym.domain.EventType
import ru.nsychev.sd.gym.domain.event.VisitEvent
import java.time.Instant

class VisitRepo : KoinComponent {
    private val eventRepo by inject<EventRepo>()

    fun mark(ticketId: Long, direction: Direction, instant: Instant) {
        eventRepo.add(VisitEvent(ticketId, direction, instant))
    }

    fun filter(filter: Bson): List<VisitEvent> {
        return eventRepo
            .find(and(
                filter,
                eq("type", EventType.VISIT_EVENT.toString())
            ))
            .toList()
            .filterIsInstance(VisitEvent::class.java)
    }

    fun all() = filter(empty())
}
