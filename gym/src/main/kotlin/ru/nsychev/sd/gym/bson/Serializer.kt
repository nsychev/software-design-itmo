package ru.nsychev.sd.gym.bson

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import de.undercouch.bson4jackson.BsonFactory
import org.bson.Document
import ru.nsychev.sd.gym.domain.EventType
import ru.nsychev.sd.gym.domain.event.Event
import ru.nsychev.sd.gym.domain.event.ExtendTicketEvent
import ru.nsychev.sd.gym.domain.event.IssueTicketEvent
import ru.nsychev.sd.gym.domain.event.VisitEvent
import java.lang.IllegalArgumentException

object Serializer {
    val jackson: ObjectMapper = ObjectMapper(BsonFactory())
        .findAndRegisterModules()
        .apply {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }

    fun decodeDocument(document: Document) = when (EventType.valueOf(document.get("type", String::class.java))) {
        EventType.EXTEND_TICKET -> jackson.convertValue<ExtendTicketEvent>(document)
        EventType.ISSUE_TICKET -> jackson.convertValue<IssueTicketEvent>(document)
        EventType.VISIT_EVENT -> jackson.convertValue<VisitEvent>(document)
    }
}
