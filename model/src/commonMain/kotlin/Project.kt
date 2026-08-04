import Project.Language
import com.github.ajalt.colormath.Color
import com.github.ajalt.colormath.model.Oklab
import io.ktor.http.Url
import kotlinx.datetime.LocalDate
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.time.Instant

@Serializable
data class Project(
    val name: String,
    val description: String?,

    val status: Status,
    val created: LocalDate,
    val languages: List<Language>,

    val urls: List<Url>,
    val license: String? = null,

    val updatedAt: Instant? = null
) {
    @Serializable
    enum class Status {
        Active,
        Unmaintained,
        Archived
    }

    @Serializable(with = LanguageAsStringSerializer::class)
    data class Language(val label: String, val color: Color? = null) {
        companion object {
            val Kotlin = Language("Kotlin", Oklab(0.588, 0.078, -0.229))
            val Java = Language("Java", Oklab(0.692, 0.164, 0.115))
            val Rust = Language("Rust", Oklab(0.738, 0.095, 0.093))
            val CSharp = Language("C#", Oklab(0.628, 0.056, -0.144))
            val entries = setOf(Kotlin, Java, Rust, CSharp)

            fun fromLabel(label: String): Language =
                entries.find { it.label.equals(label, ignoreCase = true) } ?: Language(label)
        }
    }
}

object LanguageAsStringSerializer : KSerializer<Language> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Language", STRING)

    override fun serialize(encoder: Encoder, value: Language) {
        encoder.encodeString(value.label)
    }

    override fun deserialize(decoder: Decoder): Language =
        Language.fromLabel(decoder.decodeString())
}
