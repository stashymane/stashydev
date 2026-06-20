import io.ktor.http.*
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val name: String,
    val description: String?,

    val status: Status,
    val created: LocalDate,
    val languages: List<Language>,

    val urls: List<Url>,
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
        Java,
        Rust,
        CSharp
    }
}
