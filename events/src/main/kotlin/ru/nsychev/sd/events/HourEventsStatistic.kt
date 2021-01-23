package ru.nsychev.sd.events

import ru.nsychev.sd.events.clock.AstronomicalClock
import ru.nsychev.sd.events.clock.Clock
import java.time.Instant
import kotlin.math.min

class HourEventsStatistic(
    private val clock: Clock = AstronomicalClock()
) : EventsStatistic {
    private val events: MutableMap<String, OneEventData> = mutableMapOf()

    override fun incEvent(name: String) {
        return getEvent(name).incCounter()
    }

    override fun getEventStatisticByName(name: String): List<Int> {
        return getEvent(name).getStatistics()
    }

    override fun getAllEventStatistic(): Map<String, List<Int>> {
        return events.mapValues { (_, value) ->
            value.getStatistics()
        }
    }

    override fun print() {
        events.forEach { (eventName, statistics) ->
            println("Statistics for $eventName:")
            for (number in statistics.getStatistics()) {
                print("$number ")
            }
            print("\n\n")
        }
    }

    private fun getEvent(name: String): OneEventData {
        return events.computeIfAbsent(name) { OneEventData() }
    }

    private inner class OneEventData {
        private val eventCount: MutableList<Int> = (1..60).map { 0 }.toMutableList()
        private var lastSync: Instant? = null

        private fun syncCounter() {
            val now = clock.now()

            val passed = min(now.toMinute() - lastSync.toMinute(), INTERVALS_TO_STORE.toLong()).toInt()

            eventCount.addAll(0, (1..passed).map { 0 })
            while (eventCount.size > 60) {
                eventCount.removeLast()
            }

            lastSync = now
        }

        fun incCounter() {
            syncCounter()
            eventCount[0]++
        }

        fun getStatistics(): List<Int> {
            syncCounter()
            // Make an explicit non-mutable copy.
            return listOf(*eventCount.toTypedArray())
        }
    }

    private fun Instant?.toMinute(): Long {
        return (this?.toEpochMilli() ?: 0) / INTERVAL_MILLIS
    }

    companion object {
        const val INTERVALS_TO_STORE = 60
        const val INTERVAL_MILLIS = 60000
    }
}
