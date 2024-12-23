package model

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
    }
}
