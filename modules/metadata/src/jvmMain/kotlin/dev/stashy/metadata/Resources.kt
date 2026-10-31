package dev.stashy.metadata

internal object Resources {
    fun readText(path: String): String? =
        Resources::class.java.getResourceAsStream(path)?.bufferedReader()?.use { it.readText() }
}
