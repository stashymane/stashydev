package dev.stashy.audio

/**
 * A streamable, seekable view of raw audio file bytes.
 *
 * [read] returns the number of bytes read, or -1 at end of stream.
 * [seek] repositions the stream; implementations may be approximate for compressed formats.
 */
public interface AudioStream : AutoCloseable {
    /** Total size in bytes, or -1 when unknown. */
    public val size: Long

    /** Current read position in bytes. */
    public val position: Long

    public suspend fun read(buffer: ByteArray, offset: Int, length: Int): Int

    public suspend fun seek(position: Long)

    override fun close()
}
