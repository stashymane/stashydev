package model

import kotlinx.serialization.Serializable

@Serializable
sealed class Destination {
    @Serializable
    object Home : Destination()

    @Serializable
    object Projects : Destination() {
        @Serializable
        object List : Destination()

        @Serializable
        class Id(val id: Int) : Destination()
    }
}
