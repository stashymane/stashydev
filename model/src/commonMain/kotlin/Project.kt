import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Project(
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
