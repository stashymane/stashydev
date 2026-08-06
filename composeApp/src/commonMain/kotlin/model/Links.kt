package model

import io.ktor.http.Url
import okio.ByteString.Companion.decodeHex

class Link(
    val url: Url,
    val name: String = url.host
) {
    constructor(url: String, name: String) : this(Url(url), name)

    class Group(
        val name: String,
        val links: List<Link>
    )
}

object Links {
    // fuck your bot
    val email = Link(
        "6D61696C746F3A636F6E74616374407374617368792E646576".decodeHex().utf8(),
        "Email address"
    )

    val github = Link("https://github.com/stashymane", "GitHub")
    val soundcloud = Link("https://soundcloud.com/stashymane", "SoundCloud")
    val youtube = Link("https://youtube.com/@stashymane", "YouTube")
    val xitter = Link("https://x.com/stashyymane", "X/Twitter")

    object Groups {
        val code = Link.Group("code", listOf(github))
        val content = Link.Group("content", listOf(soundcloud, youtube))
        val social = Link.Group("social", listOf(xitter))

        val All = listOf(code, content, social)
    }
}
