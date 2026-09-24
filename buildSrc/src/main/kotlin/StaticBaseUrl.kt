import org.gradle.api.Project
import org.gradle.api.provider.Provider
import java.util.*

/**
 * Origin for static metadata JSON under api/.
 *
 * Resolution order:
 * 1. `STATIC_BASE_URL` environment variable
 * 2. `-PstaticBaseUrl` / `gradle.properties`
 * 3. `staticBaseUrl` in root `local.properties`
 * 4. `static.stashy.dev`
 */
val Project.staticBaseUrl: Provider<String>
    get() = providers.environmentVariable("STATIC_BASE_URL")
        .orElse(providers.gradleProperty("staticBaseUrl"))
        .orElse(localProperty("staticBaseUrl"))
        .orElse("https://static.stashy.dev")

private fun Project.localProperty(name: String): Provider<String> =
    providers.provider {
        val file = rootProject.file("local.properties")
        if (!file.isFile) return@provider null
        file.reader().use { reader ->
            Properties().apply { load(reader) }.getProperty(name)?.takeIf(String::isNotBlank)
        }
    }
