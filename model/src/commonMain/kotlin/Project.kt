import kotlinx.serialization.Serializable
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class Project @OptIn(ExperimentalTime::class) constructor(
    val name: String,
    val url: String,
    val forks: Int,
    val stars: Int,
    val watchers: Int,
    val lastUpdate: Instant,
    val wikiUrl: String? = null,
    val releases: List<Release> = emptyList(),
) {
    @Serializable
    data class Release(
        val name: String,
        val url: String,
        val description: String
    )
}
