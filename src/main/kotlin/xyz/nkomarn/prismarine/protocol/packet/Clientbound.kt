@file:UseSerializers(ByteUUIDSerializer::class)

package xyz.nkomarn.prismarine.protocol.packet

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import xyz.nkomarn.prismarine.codec.ByteUUIDSerializer
import xyz.nkomarn.prismarine.protocol.ServerStatus
import xyz.nkomarn.prismarine.protocol.encoding.JsonString
import java.util.*

@Serializable
data class ClientboundStatusResponsePacket(
    @JsonString
    val response: ServerStatus
) : Packet

@Serializable
data class ClientboundPingPacket(val timestamp: Long) : Packet

@Serializable
data class ClientboundLoginSuccessPacket(
    val uuid: UUID,
    val username: String,
    val propertyCount: Int,
    /* todo - game profile properties */
) : Packet