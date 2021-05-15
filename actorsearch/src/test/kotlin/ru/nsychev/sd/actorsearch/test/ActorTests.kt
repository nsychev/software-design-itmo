package ru.nsychev.sd.actorsearch.test

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import ru.nsychev.sd.actorsearch.engines.EngineType
import ru.nsychev.sd.actorsearch.utils.getResults
import ru.nsychev.sd.actorsearch.utils.stub.OneshotStubServer
import ru.nsychev.sd.actorsearch.utils.stub.SleepyStubServer
import ru.nsychev.sd.actorsearch.utils.withSearchMocks
import kotlin.test.assertEquals

@DisplayName("Test actor search system")
class ActorTests {
    @Test
    fun `all servers working`() = withSearchMocks(
        OneshotStubServer(8000),
        OneshotStubServer(9000),
        OneshotStubServer(10000)
    ) {
        val results = getResults("test")

        assertEquals(15, results.size)

        val engines = mutableMapOf<EngineType, Int>()

        results.forEach {
            engines.compute(it.type) { _, old -> (old ?: 0) + 1 }
            assertEquals("http://ya.ru", it.link)
        }

        EngineType.values().forEach { it ->
            assertEquals(5, engines[it])
        }
    }

    @Test
    fun `one server times out`() = withSearchMocks(
        OneshotStubServer(8000),
        OneshotStubServer(9000),
        SleepyStubServer(10000)
    ) {
        val results = getResults("test")

        assertEquals(10, results.size)

        val engines = mutableMapOf<EngineType, Int>()

        results.forEach {
            engines.compute(it.type) { _, old -> (old ?: 0) + 1 }
            assertEquals("http://ya.ru", it.link)
        }

        assertEquals(5, engines[EngineType.APORT])
        assertEquals(5, engines[EngineType.RAMBLER])
        assertEquals(null, engines[EngineType.SPUTNIK])
    }
}
