package ru.nsychev.sd.events.clock

import java.time.Instant

class TestClock : Clock {
    private var time: Instant = Instant.now()

    fun setTime(time: Instant) {
        this.time = time
    }

    override fun now() = time
}
