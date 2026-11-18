package dev.stashy.audio

import org.khronos.webgl.ArrayBuffer
import org.khronos.webgl.Uint8Array
import org.khronos.webgl.get
import org.khronos.webgl.set
import org.w3c.dom.url.URL
import org.w3c.files.Blob
import org.w3c.files.BlobPropertyBag

internal fun ArrayBuffer.toByteArray(): ByteArray {
    val view = Uint8Array(this)
    return ByteArray(view.length) { view[it] }
}

internal fun ByteArray.toUint8Array(): Uint8Array {
    val view = Uint8Array(size)
    for (i in indices) view[i] = this[i]
    return view
}

@OptIn(ExperimentalWasmJsInterop::class)
internal fun ByteArray.toBlobUrl(mimeType: String): String {
    val blob = Blob(arrayOf<JsAny?>(toUint8Array()).toJsArray(), BlobPropertyBag(type = mimeType))
    return URL.createObjectURL(blob)
}

internal fun revokeBlobUrl(url: String) = URL.revokeObjectURL(url)
