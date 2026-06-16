import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val name: String,
    val description: String?,

    val status: Status,
    val created: LocalDate,
    val languages: List<Language>,

    val urls: List<String>,
) {
    @Serializable
    enum class Status {
        Active,
        Unmaintained,
        Archived
    }

    @Serializable
    enum class Language {
        Kotlin,
        Rust,
        CSharp
    }
}
