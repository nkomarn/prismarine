package xyz.nkomarn.prismarine.protocol.encoding

import io.ktor.utils.io.core.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.EmptySerializersModule

/**
 * implements all minecraft packet data types
 */
@OptIn(ExperimentalSerializationApi::class)
class MinecraftPacketDecoder(private val buffer: ByteReadPacket) : Decoder, CompositeDecoder {
    override val serializersModule = EmptySerializersModule()
    private var elementIndex = 0

    override fun beginStructure(descriptor: SerialDescriptor) = this
    override fun endStructure(descriptor: SerialDescriptor) {}

    /* decoder data types */
    override fun decodeByte() = buffer.readByte()
    override fun decodeShort() = buffer.readShort()
    override fun decodeInt() = TODO("Not yet implemented")
    override fun decodeLong() = buffer.readLong()
    override fun decodeFloat() = buffer.readFloat()
    override fun decodeDouble() = buffer.readDouble()
    override fun decodeBoolean() = buffer.readByte() == (1).toByte()
    override fun decodeEnum(enumDescriptor: SerialDescriptor) = buffer.readVarInt()
    override fun decodeChar() = TODO("Not yet implemented")
    override fun decodeString() = String(buffer.readBytes(buffer.readVarInt()), Charsets.UTF_8)

    /* composite decoder data types */
    override fun decodeByteElement(descriptor: SerialDescriptor, index: Int) = decodeByte()
    override fun decodeShortElement(descriptor: SerialDescriptor, index: Int) = decodeShort()
    override fun decodeIntElement(descriptor: SerialDescriptor, index: Int): Int {
        return if (descriptor.getElementAnnotations(index).any { it is VarInt }) {
            buffer.readVarInt()
        } else {
            buffer.readInt()
        }
    }
    override fun decodeLongElement(descriptor: SerialDescriptor, index: Int) = decodeLong()
    override fun decodeFloatElement(descriptor: SerialDescriptor, index: Int) = decodeFloat()
    override fun decodeDoubleElement(descriptor: SerialDescriptor, index: Int) = decodeDouble()
    override fun decodeBooleanElement(descriptor: SerialDescriptor, index: Int) = decodeBoolean()
    override fun decodeCharElement(descriptor: SerialDescriptor, index: Int) = decodeChar()
    override fun decodeStringElement(descriptor: SerialDescriptor, index: Int) = decodeString()

    /* misc decoder */
    override fun decodeInline(descriptor: SerialDescriptor) = TODO("Not yet implemented")
    override fun decodeNotNullMark() = TODO("Not yet implemented")
    override fun decodeNull() = TODO("Not yet implemented")
    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (elementIndex == descriptor.elementsCount) return CompositeDecoder.DECODE_DONE
        return elementIndex++
    }

    /* misc composite decoder */
    override fun decodeInlineElement(descriptor: SerialDescriptor, index: Int) = decodeInline(descriptor)
    override fun <T : Any> decodeNullableSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        deserializer: DeserializationStrategy<T?>,
        previousValue: T?
    ): T? = null

    override fun <T> decodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        deserializer: DeserializationStrategy<T>,
        previousValue: T?
    ): T {
        return if (descriptor.getElementAnnotations(index).any { it is JsonString }) {
            return Json.decodeFromString(deserializer, decodeString())
        } else {
            decodeSerializableValue(deserializer)
        }
    }
}