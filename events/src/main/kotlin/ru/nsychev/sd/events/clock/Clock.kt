package ru.nsychev.sd.events.clock

import java.time.Instant

interface Clock {
    fun now(): Instant
}

class AstronomicalClock : Clock {
    override fun now(): Instant = Instant.now()
}
