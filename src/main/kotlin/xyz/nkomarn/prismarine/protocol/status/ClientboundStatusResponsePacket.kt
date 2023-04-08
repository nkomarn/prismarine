package xyz.nkomarn.prismarine.protocol.status

import kotlinx.serialization.Serializable
import xyz.nkomarn.prismarine.protocol.encoding.JsonString
import xyz.nkomarn.prismarine.protocol.Packet

@Serializable
data class ClientboundStatusResponsePacket(
    @JsonString
    val response: ServerStatus
) : Packet