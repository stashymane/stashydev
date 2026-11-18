package dev.stashy.audio

public sealed interface AudioSource {
    public data class Url(
        public val url: String,
        public val headers: Map<String, String> = emptyMap(),
    ) : AudioSource

    public data class Bytes(
        public val data: ByteArray,
        public val name: String? = null,
        public val mimeType: String? = null,
    ) : AudioSource {
        override fun equals(other: Any?): Boolean =
            other is Bytes && other.name == name && other.mimeType == mimeType && other.data.contentEquals(data)

        override fun hashCode(): Int =
            31 * (31 * (name?.hashCode() ?: 0) + (mimeType?.hashCode() ?: 0)) + data.contentHashCode()
    }
}
