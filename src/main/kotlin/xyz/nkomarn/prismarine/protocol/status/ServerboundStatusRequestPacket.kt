package xyz.nkomarn.prismarine.protocol.status

import kotlinx.serialization.Serializable
import xyz.nkomarn.prismarine.protocol.ServerboundPacket

@Serializable
class ServerboundStatusRequestPacket : ServerboundPacket<StatusPacketHandler> {
    override fun handle(handler: StatusPacketHandler) = handler.handleStatusRequest(this)
}