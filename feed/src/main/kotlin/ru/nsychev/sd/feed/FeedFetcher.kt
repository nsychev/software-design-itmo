package ru.nsychev.sd.feed

import com.uchuhimo.konf.Config
import ru.nsychev.sd.feed.api.VKClient
import java.io.Closeable
import java.time.Instant

class FeedFetcher(
    private val client: VKClient
) : Closeable {
    constructor(config: Config) : this(VKClient(config))

    suspend fun getStatsByHashtag(hashtag: String, hours: Int = 24): List<Int> {
        val currentTime = Instant.now()
        return (0 until hours).map { n ->
            client.newsfeedSearch(
                hashtag,
                0,
                currentTime.minusSeconds(60L * 60 * (n + 1)).epochSecond,
                currentTime.minusSeconds(60L * 60 * n + 1).epochSecond
            ).totalCount
        }
    }

    override fun close() {
        client.close()
    }
}
