package ru.nsychev.sd.events.utils

fun listOfSegments(vararg segments: ListSegment): List<Int> {
    return mutableListOf<Int>().apply {
        segments.map { segment ->
            addAll((1..segment.count).map { segment.value })
        }
    }
}

data class ListSegment(val value: Int, val count: Int)

fun repeat(value: Int, count: Int) = ListSegment(value, count)

fun one(value: Int) = ListSegment(value, 1)
