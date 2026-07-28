import com.github.ajalt.colormath.Color
import com.github.ajalt.colormath.model.Oklab
import io.ktor.http.Url
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
    val license: String? = null
) {
    @Serializable
    enum class Status {
        Active,
        Unmaintained,
        Archived
    }

    @Serializable
    enum class Language(val label: String, val color: Color) {
        Kotlin("Kotlin", Oklab(0.588, 0.078, -0.229)),
        Java("Java", Oklab(0.692, 0.164, 0.115)),
        Rust("Rust", Oklab(0.738, 0.095, 0.093)),
        CSharp("C#", Oklab(0.628, 0.056, -0.144))
    }
}
