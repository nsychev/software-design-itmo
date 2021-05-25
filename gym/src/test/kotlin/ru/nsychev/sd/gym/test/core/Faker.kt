package ru.nsychev.sd.gym.test.core

import java.time.Instant

object Faker {
    private var id = 20000L
    fun id() = id++

    private var instant = 1000000L
    fun instant(): Instant {
        instant += 1000
        return Instant.ofEpochMilli(instant)
    }

    private var days = 30L
    fun days() = days++
}
