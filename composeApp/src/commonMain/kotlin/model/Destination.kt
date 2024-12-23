package model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
sealed class Destination(
    @Transient
    val parent: Destination? = null
) {
    @Serializable
    @SerialName("home")
    data object Home : Destination()

    @Serializable
    data object Projects : Destination() {
        @Serializable
        @SerialName("list")
        data object List : Destination(Projects)

        @Serializable
        @SerialName("id")
        data class Id(val id: Int) : Destination(Projects)
    }

    fun name() = toString()

    //TODO check out ktor's implementation of link generation from types
    @OptIn(ExperimentalSerializationApi::class)
    fun toUri(): String = buildString {
        append(name())
        var needle: Destination? = this@Destination.parent
        while (needle != null) {
            insert(0, "${needle.name()}/")
            needle = needle.parent
        }
    }
}
