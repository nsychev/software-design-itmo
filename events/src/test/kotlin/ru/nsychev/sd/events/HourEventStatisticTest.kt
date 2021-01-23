package ru.nsychev.sd.events

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import ru.nsychev.sd.events.clock.TestClock
import ru.nsychev.sd.events.utils.listOfSegments
import ru.nsychev.sd.events.utils.one
import ru.nsychev.sd.events.utils.repeat
import java.time.Instant

class HourEventStatisticTest {
    private val clock = TestClock()

    @Test
    fun shouldReturnEmptyMap() {
        val eventsStatistic = HourEventsStatistic(clock)
        assertEquals(0, eventsStatistic.getAllEventStatistic().size)
    }

    @Test
    fun shouldReturnEvent() {
        val eventsStatistic = HourEventsStatistic(clock)

        clock.setTime(Instant.parse("2021-01-23T00:00:00Z"))
        eventsStatistic.incEvent("test")

        assertEquals(
                listOfSegments(one(1), repeat(0, 59)),
                eventsStatistic.getEventStatisticByName("test")
        )
    }

    @Test
    fun shouldReturnEmpty() {
        val eventsStatistic = HourEventsStatistic(clock)

        assertEquals(
                listOfSegments(repeat(0, 60)),
                eventsStatistic.getEventStatisticByName("test")
        )
    }

    @Test
    fun shouldShiftElements() {
        val eventsStatistic = HourEventsStatistic(clock)

        clock.setTime(Instant.parse("2021-01-23T00:00:00Z"))
        eventsStatistic.incEvent("test")

        clock.setTime(Instant.parse("2021-01-23T00:13:37Z"))
        eventsStatistic.incEvent("test")

        assertEquals(
                listOfSegments(one(1), repeat(0, 12), one(1), repeat(0, 46)),
                eventsStatistic.getEventStatisticByName("test")
        )
    }

    @Test
    fun shouldReturnOneEvent() {
        val eventsStatistic = HourEventsStatistic(clock)
        eventsStatistic.incEvent("test")

        assertEquals(
                mapOf("test" to listOfSegments(one(1), repeat(0, 59))),
                eventsStatistic.getAllEventStatistic()
        )
    }

    @Test
    fun shouldReturnSeveralEvents() {
        val eventsStatistic = HourEventsStatistic(clock)
        eventsStatistic.incEvent("test")
        eventsStatistic.incEvent("other")

        assertEquals(
                mapOf(
                        "test" to listOfSegments(one(1), repeat(0, 59)),
                        "other" to listOfSegments(one(1), repeat(0, 59))
                ),
                eventsStatistic.getAllEventStatistic()
        )
    }

    @Test
    fun shouldSyncAllEvents() {
        val eventsStatistic = HourEventsStatistic(clock)

        clock.setTime(Instant.parse("2021-01-23T00:00:00Z"))
        eventsStatistic.incEvent("test")

        clock.setTime(Instant.parse("2021-01-23T00:13:37Z"))
        eventsStatistic.incEvent("other")

        assertEquals(
                mapOf(
                        "test" to listOfSegments(repeat(0, 13), one(1), repeat(0, 46)),
                        "other" to listOfSegments(one(1), repeat(0, 59))
                ),
                eventsStatistic.getAllEventStatistic()
        )
    }

    @Test
    fun shouldCountSeveralEventsDuringMinute() {
        val eventsStatistic = HourEventsStatistic(clock)

        clock.setTime(Instant.parse("2021-01-23T00:00:00Z"))
        eventsStatistic.incEvent("test")

        clock.setTime(Instant.parse("2021-01-23T00:00:47Z"))
        eventsStatistic.incEvent("test")

        assertEquals(
                listOfSegments(one(2), repeat(0, 59)),
                eventsStatistic.getEventStatisticByName("test")
        )
    }

    @Test
    fun shouldResetCounterAfterHour() {
        val eventsStatistic = HourEventsStatistic(clock)

        clock.setTime(Instant.parse("2021-01-23T00:00:00Z"))
        eventsStatistic.incEvent("test")

        clock.setTime(Instant.parse("2021-01-23T01:00:00Z"))

        assertEquals(
                listOfSegments(repeat(0, 60)),
                eventsStatistic.getEventStatisticByName("test")
        )
    }

    @Test
    fun shouldNotForgetEventAfterHour() {
        val eventsStatistic = HourEventsStatistic(clock)

        clock.setTime(Instant.parse("2021-01-23T00:00:00Z"))
        eventsStatistic.incEvent("test")

        clock.setTime(Instant.parse("2021-01-23T01:00:00Z"))
        eventsStatistic.incEvent("other")

        assertEquals(
                mapOf(
                        "test" to listOfSegments(repeat(0, 60)),
                        "other" to listOfSegments(one(1), repeat(0, 59))
                ),
                eventsStatistic.getAllEventStatistic()
        )
    }
}
