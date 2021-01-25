package ru.nsychev.sd.feed.conf

import com.uchuhimo.konf.ConfigSpec

object FeedSpec : ConfigSpec() {
    val proto by optional("https")
    val host by required<String>()
    val port by optional(443)
    val version by optional("5.126")
    val accessToken by required<String>()
}
