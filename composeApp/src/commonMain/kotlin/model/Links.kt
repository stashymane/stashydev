package model

import io.ktor.http.Url
import okio.ByteString.Companion.decodeHex

object Links {
    // fuck your bot
    val email = Url("6D61696C746F3A636F6E74616374407374617368792E646576".decodeHex().utf8())

    val github = Url("https://github.com/stashymane")
    val soundcloud = Url("https://soundcloud.com/stashymane")
    val youtube = Url("https://youtube.com/@stashymane")
    val xitter = Url("https://x.com/stashyymane")
}
