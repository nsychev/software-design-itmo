package ru.nsychev.sd.feed

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.anyString
import org.mockito.Mockito.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import ru.nsychev.sd.feed.api.ListResponse
import ru.nsychev.sd.feed.api.VKClient
import java.time.Instant
import kotlin.test.assertEquals

class FeedFetcherTest {
    private lateinit var vkClient: VKClient

    @BeforeEach
    fun prepareMock() {
        vkClient = mock(VKClient::class.java)
    }

    @Test
    fun shouldReturnCorrectValue() = runBlocking {
        `when`(vkClient.newsfeedSearch(
            anyString(), anyInt(), anyLong(), anyLong()
        )).thenReturn(ListResponse(listOf(), 0, 123, null))

        val feedFetcher = FeedFetcher(vkClient)

        assertEquals(
            listOf(123),
            feedFetcher.getStatsByHashtag("#test", 1)
        )
    }

    @Test
    fun shouldSortValues() = runBlocking {
        `when`(vkClient.newsfeedSearch(
            anyString(), anyInt(),
            eq(Instant.now().minusSeconds(60L * 60).epochSecond),
            anyLong()
        )).thenReturn(ListResponse(listOf(), 0, 123, null))

        `when`(vkClient.newsfeedSearch(
            anyString(), anyInt(),
            eq(Instant.now().minusSeconds(60L * 60 * 2).epochSecond),
            anyLong()
        )).thenReturn(ListResponse(listOf(), 0, 456, null))

        val feedFetcher = FeedFetcher(vkClient)

        assertEquals(
            listOf(123, 456),
            feedFetcher.getStatsByHashtag("#test", 2)
        )
    }

    @Test
    fun shouldCallClientNTimes() {
        runBlocking {
            `when`(vkClient.newsfeedSearch(
                anyString(), anyInt(), anyLong(), anyLong()
            )).thenReturn(ListResponse(listOf(), 0, 0, null))

            val feedFetcher = FeedFetcher(vkClient)
            val startTime = Instant.now()
            feedFetcher.getStatsByHashtag("#test", 2)

            verify(vkClient, times(1)).newsfeedSearch(
                "#test", 0,
                startTime.minusSeconds(60L * 60).epochSecond,
                startTime.minusSeconds(1).epochSecond
            )

            verify(vkClient, times(1)).newsfeedSearch(
                "#test", 0,
                startTime.minusSeconds(60L * 60 * 2).epochSecond,
                startTime.minusSeconds(60L * 60 + 1).epochSecond
            )

            verify(vkClient, times(2)).newsfeedSearch(
                anyString(), anyInt(), anyLong(), anyLong()
            )
        }
    }
}
