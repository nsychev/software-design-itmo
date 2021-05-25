package ru.nsychev.sd.gym.repo

import com.fasterxml.jackson.module.kotlin.convertValue
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import org.bson.conversions.Bson
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.nsychev.sd.gym.domain.event.Event
import ru.nsychev.sd.gym.bson.Serializer
import ru.nsychev.sd.gym.bson.Serializer.jackson

class EventRepo : KoinComponent {
    private val database by inject<MongoDatabase>()
    private val collection by lazy { database.getCollection("gym") }

    fun add(event: Event) {
        collection.insertOne(jackson.convertValue(event))
    }

    fun find(filter: Bson): List<Event> {
        return collection.find(filter)
            .toList()
            .map(Serializer::decodeDocument)
    }

    fun clean() {
        collection.deleteMany(Filters.empty())
    }
}
