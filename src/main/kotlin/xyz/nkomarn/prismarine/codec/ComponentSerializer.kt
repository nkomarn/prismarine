package xyz.nkomarn.prismarine.codec

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonEncoder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Component::class)
object ComponentSerializer : KSerializer<Component> {
    override val descriptor = PrimitiveSerialDescriptor("Component", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Component {
        return GsonComponentSerializer.gson().deserialize(decoder.decodeString())
    }

    override fun serialize(encoder: Encoder, value: Component) {
        encoder.encodeString(GsonComponentSerializer.gson().serialize(value))
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Component::class)
object JsonComponentSerializer : KSerializer<Component> {
    override val descriptor = PrimitiveSerialDescriptor("Component", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Component {
        throw UnsupportedOperationException()
    }

    override fun serialize(encoder: Encoder, value: Component) {
        if (encoder is JsonEncoder) {
            val json = GsonComponentSerializer.gson().serialize(value)
            return encoder.encodeJsonElement(Json.parseToJsonElement(json))
        }

        throw UnsupportedOperationException()
    }
}
