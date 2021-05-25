package ru.nsychev.sd.gym.test.service

import com.mongodb.client.model.Filters.empty
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.nsychev.sd.gym.domain.TicketInfo
import ru.nsychev.sd.gym.domain.event.IssueTicketEvent
import ru.nsychev.sd.gym.service.ManagerService
import ru.nsychev.sd.gym.test.core.BaseTest
import ru.nsychev.sd.gym.test.core.Faker
import ru.nsychev.sd.gym.test.core.TestApplication
import java.time.Instant
import kotlin.test.assertEquals

class ManagerServiceTest : BaseTest() {
    private val managerService: ManagerService by lazy { TestApplication.get() }

    @Test
    fun `should issue ticket`() {
        val instant = Faker.instant()
        val days = Faker.days()

        val ticketId = managerService.issue(instant, days)

        val events = eventRepo.find(empty())

        assertEquals(listOf(IssueTicketEvent(ticketId, instant, days)), events)
    }

    @Test
    fun `should get ticket`() {
        val id = Faker.id()
        val instant = Faker.instant()
        val days = Faker.days()

        eventRepo.add(IssueTicketEvent(id, instant, days))

        val ticket = managerService.get(id)

        assertEquals(TicketInfo(id, instant, days), ticket)
    }

    @Test
    fun `should prolong ticket`() {
        val instant = Faker.instant()
        val days = Faker.days()

        val ticketId = managerService.issue(instant, days)

        val extendDays = Faker.days()
        managerService.extend(ticketId, extendDays)

        val ticket = managerService.get(ticketId)
        assertEquals(TicketInfo(ticketId, instant, days + extendDays), ticket)
    }

    @Test
    fun `should throw when no such ticket`() {
        assertThrows<IllegalArgumentException> {
            managerService.get(Faker.id())
        }
    }
}
