package xyz.nkomarn.prismarine.protocol.status

import kotlinx.serialization.Serializable
import xyz.nkomarn.prismarine.protocol.Packet

@Serializable
data class ClientboundPongPacket(val timestamp: Long) : Packet