package ru.nsychev.sd.feed

import com.uchuhimo.konf.Config
import kotlinx.coroutines.runBlocking
import ru.nsychev.sd.feed.api.VKException
import ru.nsychev.sd.feed.conf.FeedSpec
import java.time.Instant
import kotlin.system.exitProcess

suspend fun main(args: Array<String>) {
    if (args.size != 1) {
        System.err.println("Pass hashtag as argument.")
        exitProcess(1)
    }

    val tag = "#${args[0]}"

    val config = Config {
        addSpec(FeedSpec)
    }.from.env()

    config[FeedSpec.proto] = "https"
    config[FeedSpec.host] = "api.vk.com"
    config[FeedSpec.port] = 443

    val currentTime = Instant.now()
    val postsPerHour = try {
        runBlocking { FeedFetcher(config).use { it.getStatsByHashtag(tag) } }
    } catch (exc: VKException) {
        System.err.println("API failed: $exc")
        exitProcess(1)
    }
    println("Posts tagged $tag per hour:")
    for (n in postsPerHour.indices) {
        println("Since ${currentTime.minusSeconds(60L * 60 * (n + 1))}:\t${postsPerHour[n]}")
    }
}
