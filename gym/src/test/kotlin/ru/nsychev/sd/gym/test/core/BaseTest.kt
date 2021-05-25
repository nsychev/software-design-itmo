package ru.nsychev.sd.gym.test.core

import org.junit.jupiter.api.BeforeEach
import ru.nsychev.sd.gym.repo.EventRepo

open class BaseTest {
    protected val eventRepo: EventRepo by lazy { TestApplication.get() }

    @BeforeEach
    fun clean() {
        eventRepo.clean()
    }
}
