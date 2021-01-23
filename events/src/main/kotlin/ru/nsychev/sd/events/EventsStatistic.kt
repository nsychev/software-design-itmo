package ru.nsychev.sd.events

interface EventsStatistic {
    fun incEvent(name: String)
    fun getEventStatisticByName(name: String): List<Int>
    fun getAllEventStatistic(): Map<String, List<Int>>
    fun print()
}
