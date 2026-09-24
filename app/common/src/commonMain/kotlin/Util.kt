import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

fun Instant.toRelativeString(now: Instant = Clock.System.now()): String {
    val ago = now - this
    return when {
        ago < 1.minutes -> "just now"
        ago < 1.hours -> "${ago.inWholeMinutes}m ago"
        ago < 1.days -> "${ago.inWholeHours}h ago"
        ago < 30.days -> "${ago.inWholeDays}d ago"
        ago < 365.days -> "${ago.inWholeDays / 30}mo ago"
        else -> "${ago.inWholeDays / 365}y ago"
    }
}
