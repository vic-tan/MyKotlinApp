package com.onlineaginguniversity.login.utils

import okio.Buffer
import okio.ForwardingSink
import okio.Sink

/**
 * Encodes writes to a [Sink] on the fly using Base64.
 *
 * <p>Chunked writing is unsupported; the written Base64 output is always finalised. However, partial writing (not
 * draining the full source) is supported.
 */
class Base64Sink(delegate: Sink) : ForwardingSink(delegate) {

    override fun write(source: Buffer, byteCount: Long) {
        // Read the requested number of bytes (or all available) from source
        val bytesToRead = byteCount.coerceAtMost(source.size)
        val decoded = source.readByteString(bytesToRead)

        // Base64-encode
        val encoded = decoded.base64()

        val encodedSink = Buffer()
        encodedSink.writeUtf8(encoded)
        super.write(encodedSink, encodedSink.size)
    }


    private fun test() {
        val utf8Sink = Buffer().writeUtf8("okio oh my¿¡")
        val base64Buffer = Buffer()
        val base64Sink = Base64Sink(base64Buffer)

        base64Sink.write(utf8Sink, Long.MAX_VALUE)
//        assertThat(base64Buffer.readUtf8()).isEqualTo("b2tpbyBvaCBtecK/wqE=")
    }
}