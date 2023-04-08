package xyz.nkomarn.prismarine.protocol.status

import kotlinx.serialization.Serializable
import xyz.nkomarn.prismarine.protocol.ServerboundPacket

@Serializable
data class ServerboundPingPacket(val timestamp: Long) : ServerboundPacket<StatusPacketHandler> {
    override fun handle(handler: StatusPacketHandler) = handler.handlePing(this)
}