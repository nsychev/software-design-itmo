package ru.nsychev.sd.gym.service

import com.mongodb.client.model.Filters.and
import com.mongodb.client.model.Filters.eq
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.nsychev.sd.gym.domain.Direction
import ru.nsychev.sd.gym.repo.VisitRepo
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import kotlin.math.max

class ReportService : KoinComponent {
    private val visitRepo by inject<VisitRepo>()

    fun daily(ticketId: Long): Map<LocalDate, Int> =
        visitRepo
            .filter(
                and(
                    eq("ticketId", ticketId),
                    eq("direction", Direction.ENTER.toString())
                )
            )
            .groupBy { LocalDate.ofInstant(it.timestamp, ZoneOffset.UTC) }
            .mapValues { it.value.size }

    fun averageCount(): Double =
        visitRepo
            .filter(eq("direction", Direction.ENTER.toString()))
            .groupBy { it.ticketId }
            .map { it.value.size }
            .let { it.sum().toDouble() / max(it.size, 1) }

    fun averageDuration(): Double =
        visitRepo
            .all()
            .sortedBy { it.timestamp }
            .fold(DurationHolder(0, 0, mapOf())) { holder, visit ->
                when (visit.direction) {
                    Direction.ENTER -> DurationHolder(
                        holder.visits,
                        holder.duration,
                        holder.enterTime.plus(visit.ticketId to visit.timestamp)
                    )
                    Direction.EXIT -> {
                        val duration = Duration.between(
                            holder.enterTime
                                .getOrElse(visit.ticketId) { throw IllegalArgumentException("Exit-only member") },
                            visit.timestamp
                        ).toSeconds()
                        DurationHolder(
                            holder.visits + 1,
                            holder.duration + duration,
                            holder.enterTime.minus(visit.ticketId)
                        )
                    }
                }
            }
            .let {
                it.duration.toDouble() / max(it.visits, 1)
            }

    private data class DurationHolder(
        val visits: Long,
        val duration: Long,
        val enterTime: Map<Long, Instant>
    )
}
