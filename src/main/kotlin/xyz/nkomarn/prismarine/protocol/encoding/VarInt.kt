package xyz.nkomarn.prismarine.protocol.encoding

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialInfo
import kotlin.experimental.and
import kotlin.experimental.or

@SerialInfo
@Target(AnnotationTarget.PROPERTY)
@OptIn(ExperimentalSerializationApi::class)
annotation class VarInt

suspend fun ByteReadChannel.readVarInt(): Int {
    var numRead = 0
    var result = 0
    var read: Byte

    do {
        read = readByte()
        val value = (read and 127).toInt()
        result = result or (value shl 7 * numRead)
        numRead++

        if (numRead > 5) {
            throw RuntimeException("VarInt is too big")
        }
    } while (read and 128.toByte() != 0.toByte())

    return result
}

fun ByteReadPacket.readVarInt(): Int {
    var numRead = 0
    var result = 0
    var read: Byte

    do {
        read = readByte()
        val value = (read and 127).toInt()
        result = result or (value shl 7 * numRead)
        numRead++

        if (numRead > 5) {
            throw RuntimeException("VarInt is too big")
        }
    } while (read and 128.toByte() != 0.toByte())

    return result
}

suspend fun ByteWriteChannel.writeVarInt(number: Int) {
    var value = number

    do {
        var temp = (value and 127).toByte()
        value = value ushr 7

        if (value != 0) {
            temp = temp or 128.toByte()
        }

        writeByte(temp)
    } while (value != 0)
}

fun BytePacketBuilder.writeVarInt(number: Int) {
    var value = number

    do {
        var temp = (value and 127).toByte()
        value = value ushr 7

        if (value != 0) {
            temp = temp or 128.toByte()
        }

        writeByte(temp)
    } while (value != 0)
}
