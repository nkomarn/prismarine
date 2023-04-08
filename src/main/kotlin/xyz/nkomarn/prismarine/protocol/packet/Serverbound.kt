@file:UseSerializers(ByteUUIDSerializer::class)

package xyz.nkomarn.prismarine.protocol.packet

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import xyz.nkomarn.prismarine.codec.ByteUUIDSerializer
import xyz.nkomarn.prismarine.protocol.encoding.VarInt
import xyz.nkomarn.prismarine.protocol.connection.ConnectionState
import xyz.nkomarn.prismarine.protocol.handler.PacketHandler
import java.util.*

@Serializable
data class ServerboundHandshakePacket(
    @VarInt
    val protocolVersion: Int,
    val serverAddress: String,
    val serverPort: Short,
    val nextState: ConnectionState,
) : ServerboundPacket {
    override fun handle(handler: PacketHandler) = handler.handleHandshake(this)
}

@Serializable
class ServerboundStatusRequestPacket : ServerboundPacket {
    override fun handle(handler: PacketHandler) = handler.handleStatusRequest(this)
}

@Serializable
data class ServerboundPingPacket(val timestamp: Long) : ServerboundPacket {
    override fun handle(handler: PacketHandler) = handler.handlePingRequest(this)
}

@Serializable
data class ServerboundLoginPacket(
    val username: String,
    val hasUUID: Boolean,
    /* todo - this field is only sent when the above is true */
    val uuid: UUID?,
) : ServerboundPacket {
    override fun handle(handler: PacketHandler) = handler.handleLogin(this)
}