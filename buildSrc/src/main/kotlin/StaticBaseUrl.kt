import org.gradle.api.Project
import org.gradle.api.provider.Provider

/**
 * Origin for static metadata JSON under api/.
 * From [STATIC_BASE_URL] or `-PstaticBaseUrl`, defaulting to `static.stashy.dev`.
 */
val Project.staticBaseUrl: Provider<String>
    get() = providers.environmentVariable("STATIC_BASE_URL")
        .orElse(providers.gradleProperty("staticBaseUrl"))
        .orElse("static.stashy.dev")
