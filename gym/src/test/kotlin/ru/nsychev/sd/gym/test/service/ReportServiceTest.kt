package ru.nsychev.sd.gym.test.service

import org.junit.jupiter.api.Test
import ru.nsychev.sd.gym.domain.Direction
import ru.nsychev.sd.gym.domain.event.IssueTicketEvent
import ru.nsychev.sd.gym.domain.event.VisitEvent
import ru.nsychev.sd.gym.service.ReportService
import ru.nsychev.sd.gym.test.core.BaseTest
import ru.nsychev.sd.gym.test.core.Faker
import ru.nsychev.sd.gym.test.core.TestApplication
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import kotlin.test.assertEquals

class ReportServiceTest : BaseTest() {
    private val reportService: ReportService by lazy { TestApplication.get() }

    @Test
    fun `get daily stats`() {
        val id = Faker.id()
        val instant = Faker.roundedInstant()
        val days = Faker.days()

        eventRepo.add(IssueTicketEvent(id, instant, days))
        eventRepo.add(VisitEvent(id, Direction.ENTER, instant + Duration.ofMinutes(1)))
        eventRepo.add(VisitEvent(id, Direction.EXIT, instant + Duration.ofMinutes(2)))
        eventRepo.add(VisitEvent(id, Direction.ENTER, instant + Duration.ofMinutes(3)))
        eventRepo.add(VisitEvent(id, Direction.EXIT, instant + Duration.ofMinutes(4)))
        eventRepo.add(VisitEvent(id, Direction.ENTER, instant + Duration.ofMinutes(1441)))
        eventRepo.add(VisitEvent(id, Direction.EXIT, instant + Duration.ofMinutes(1442)))

        val stats = reportService.daily(id)

        assertEquals(
            mapOf(
                LocalDate.ofInstant(instant, ZoneOffset.UTC) to 2,
                LocalDate.ofInstant(instant + Duration.ofDays(1), ZoneOffset.UTC) to 1
            ),
            stats
        )
    }

    @Test
    fun `get average count`() {
        val id1 = Faker.id()
        val id2 = Faker.id()
        val instant = Faker.roundedInstant()
        val days = Faker.days()

        eventRepo.add(IssueTicketEvent(id1, instant, days))
        eventRepo.add(IssueTicketEvent(id2, instant, days))
        eventRepo.add(VisitEvent(id1, Direction.ENTER, instant + Duration.ofMinutes(1)))
        eventRepo.add(VisitEvent(id1, Direction.EXIT, instant + Duration.ofMinutes(2)))
        eventRepo.add(VisitEvent(id1, Direction.ENTER, instant + Duration.ofMinutes(3)))
        eventRepo.add(VisitEvent(id1, Direction.EXIT, instant + Duration.ofMinutes(4)))
        eventRepo.add(VisitEvent(id2, Direction.ENTER, instant + Duration.ofMinutes(1441)))
        eventRepo.add(VisitEvent(id2, Direction.EXIT, instant + Duration.ofMinutes(1442)))

        val average = reportService.averageCount()
        assertEquals(1.5, average)
    }

    @Test
    fun `get average count of empty set`() {
        val average = reportService.averageCount()
        assertEquals(0.0, average)
    }

    @Test
    fun `get average duration`() {
        val id = Faker.id()
        val instant = Faker.roundedInstant()
        val days = Faker.days()

        eventRepo.add(IssueTicketEvent(id, instant, days))
        eventRepo.add(VisitEvent(id, Direction.ENTER, instant + Duration.ofSeconds(1)))
        eventRepo.add(VisitEvent(id, Direction.EXIT, instant + Duration.ofSeconds(4)))
        eventRepo.add(VisitEvent(id, Direction.ENTER, instant + Duration.ofSeconds(10)))
        eventRepo.add(VisitEvent(id, Direction.EXIT, instant + Duration.ofSeconds(12)))
        eventRepo.add(VisitEvent(id, Direction.ENTER, instant + Duration.ofSeconds(10000)))
        eventRepo.add(VisitEvent(id, Direction.EXIT, instant + Duration.ofSeconds(10007)))

        val average = reportService.averageDuration()
        assertEquals(4.0, average)
    }

    @Test
    fun `get average duration of empty set`() {
        val average = reportService.averageDuration()
        assertEquals(0.0, average)
    }

    private fun Faker.roundedInstant(): Instant = instant().let {
        it - Duration.ofHours(it.atZone(ZoneOffset.UTC).hour.toLong())
    }
}
