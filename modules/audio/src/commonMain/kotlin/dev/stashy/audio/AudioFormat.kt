package dev.stashy.audio

public data class AudioFormat(
    public val sampleRate: Int,
    public val channels: Int,
    public val bitsPerSample: Int,
) {
    public companion object {
        /** Used on platforms where the container format is decoded natively and not inspected. */
        public val Unknown: AudioFormat = AudioFormat(0, 0, 0)
    }
}
