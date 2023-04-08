package xyz.nkomarn.prismarine.protocol.encoding

import io.ktor.utils.io.core.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.EmptySerializersModule

/**
 * implements all minecraft packet data types
 */
@OptIn(ExperimentalSerializationApi::class)
class MinecraftPacketEncoder(private val buffer: BytePacketBuilder) : Encoder, CompositeEncoder {
    override val serializersModule = EmptySerializersModule()

    override fun beginStructure(descriptor: SerialDescriptor) = this
    override fun endStructure(descriptor: SerialDescriptor) {}

    /* encoder data types */
    override fun encodeByte(value: Byte) = buffer.writeByte(value)
    override fun encodeShort(value: Short) = buffer.writeShort(value)
    override fun encodeInt(value: Int) = TODO("Not yet implemented")
    override fun encodeLong(value: Long) = buffer.writeLong(value)
    override fun encodeFloat(value: Float) = buffer.writeFloat(value)
    override fun encodeDouble(value: Double) = buffer.writeDouble(value)
    override fun encodeBoolean(value: Boolean ) = buffer.writeByte(if (value) 1 else 0)
    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) = buffer.writeVarInt(index)
    override fun encodeChar(value: Char) = TODO("Not yet implemented")
    override fun encodeString(value: String) {
        buffer.writeVarInt(value.length)
        buffer.writeFully(value.toByteArray(Charsets.UTF_8))
    }

    /* composite encoder data types */
    override fun encodeByteElement(descriptor: SerialDescriptor, index: Int, value: Byte) = encodeByte(value)
    override fun encodeShortElement(descriptor: SerialDescriptor, index: Int, value: Short) = encodeShort(value)
    override fun encodeIntElement(descriptor: SerialDescriptor, index: Int, value: Int) {
        if (descriptor.getElementAnnotations(index).any { it is VarInt }) {
            buffer.writeVarInt(value)
        } else {
            buffer.writeInt(value)
        }
    }
    override fun encodeLongElement(descriptor: SerialDescriptor, index: Int, value: Long) = encodeLong(value)
    override fun encodeFloatElement(descriptor: SerialDescriptor, index: Int, value: Float) = encodeFloat(value)
    override fun encodeDoubleElement(descriptor: SerialDescriptor, index: Int, value: Double) = encodeDouble(value)
    override fun encodeBooleanElement(descriptor: SerialDescriptor, index: Int, value: Boolean) = encodeBoolean(value)
    override fun encodeCharElement(descriptor: SerialDescriptor, index: Int, value: Char) = encodeChar(value)
    override fun encodeStringElement(descriptor: SerialDescriptor, index: Int, value: String) = encodeString(value)

    /* misc encoder */
    override fun encodeInline(descriptor: SerialDescriptor) = TODO("Not yet implemented")
    override fun encodeNotNullMark() = TODO("Not yet implemented")
    override fun encodeNull() = TODO("Not yet implemented")

    /* misc composite encoder */
    override fun encodeInlineElement(descriptor: SerialDescriptor, index: Int) = encodeInline(descriptor)
    override fun <T : Any> encodeNullableSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        serializer: SerializationStrategy<T>,
        value: T?
    ) {
        TODO("Not yet implemented")
    }
    override fun <T> encodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        serializer: SerializationStrategy<T>,
        value: T
    ) {
        if (descriptor.getElementAnnotations(index).any { it is JsonString }) {
            return encodeString(JSON.encodeToString(serializer, value))
        }

        encodeSerializableValue(serializer, value)
    }

    private companion object {
        val JSON = Json { explicitNulls = false }
    }
}