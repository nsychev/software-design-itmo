package ru.nsychev.sd.gym.test.service

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import ru.nsychev.sd.gym.domain.Direction
import ru.nsychev.sd.gym.domain.event.ExtendTicketEvent
import ru.nsychev.sd.gym.domain.event.IssueTicketEvent
import ru.nsychev.sd.gym.service.TurnstileService
import ru.nsychev.sd.gym.test.core.BaseTest
import ru.nsychev.sd.gym.test.core.Faker
import ru.nsychev.sd.gym.test.core.TestApplication
import java.time.Duration

class TurnstileServiceTest : BaseTest() {
    private val turnstileService: TurnstileService by lazy { TestApplication.get() }

    @Test
    fun `should allow`() {
        val id = Faker.id()
        val instant = Faker.instant()
        val days = Faker.days()

        eventRepo.add(IssueTicketEvent(id, instant, days))

        assertDoesNotThrow {
            turnstileService.pass(id, Direction.ENTER, instant)
        }
    }

    @Test
    fun `should deny`() {
        val id = Faker.id()
        val instant = Faker.instant()
        val days = Faker.days()

        eventRepo.add(IssueTicketEvent(id, instant, days))

        assertThrows<IllegalArgumentException> {
            turnstileService.pass(id, Direction.ENTER, instant + Duration.ofDays(days + 1))
        }
    }

    @Test
    fun `should allow after extend`() {
        val id = Faker.id()
        val instant = Faker.instant()
        val days = Faker.days()

        eventRepo.add(IssueTicketEvent(id, instant, days))
        eventRepo.add(ExtendTicketEvent(id, 2L))

        assertDoesNotThrow {
            turnstileService.pass(id, Direction.ENTER, instant + Duration.ofDays(days + 1))
        }
    }
}
