package ru.nsychev.sd.lru

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LRUCacheTest {
    @Test
    fun should_store_value() {
        val cache = LRUCache<Int, Int>(10)
        cache[5] = 7
        assertEquals(7, cache[5])
    }

    @Test
    fun should_store_several_values() {
        val cache = LRUCache<Int, Int>(10)
        cache[1] = 2
        cache[3] = 4
        assertEquals(2, cache[1])
        assertEquals(4, cache[3])
    }

    @Test
    fun should_remove_eldest() {
        val cache = LRUCache<Int, Int>(3)
        cache[1] = 2
        cache[3] = 4
        cache[5] = 6
        cache[7] = 8
        assertEquals(null, cache[1])
        assertEquals(4, cache[3])
        assertEquals(6, cache[5])
        assertEquals(8, cache[7])
    }

    @Test
    fun should_replace_values() {
        val cache = LRUCache<Int, Int>(10)
        cache[1] = 2
        cache[1] = 3
        assertEquals(3, cache[1])
    }

    @Test
    fun should_remove_eldest_after_replace() {
        val cache = LRUCache<Int, Int>(2)
        cache[1] = 2
        cache[1] = 3
        cache[4] = 5
        assertEquals(3, cache[1])
        assertEquals(5, cache[4])
        cache[1] = 6
        cache[7] = 8
        assertEquals(6, cache[1])
        assertEquals(null, cache[4])
        assertEquals(8, cache[7])
    }

    @Test
    fun should_update_ttl_after_get() {
        val cache = LRUCache<Int, Int>(2)
        cache[1] = 2
        cache[3] = 4
        assertEquals(2, cache[1])

        cache[5] = 6
        assertEquals(2, cache[1])
        assertEquals(null, cache[3])
        assertEquals(6, cache[5])
    }
}
