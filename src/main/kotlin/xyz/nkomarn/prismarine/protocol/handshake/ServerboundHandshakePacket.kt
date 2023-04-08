package xyz.nkomarn.prismarine.protocol.handshake

import kotlinx.serialization.Serializable
import xyz.nkomarn.prismarine.network.connection.ConnectionState
import xyz.nkomarn.prismarine.protocol.encoding.VarInt
import xyz.nkomarn.prismarine.protocol.ServerboundPacket


@Serializable
data class ServerboundHandshakePacket(
    @VarInt
    val protocolVersion: Int,
    val serverAddress: String,
    val serverPort: Short,
    val nextState: ConnectionState,
) : ServerboundPacket<HandshakePacketHandler> {
    override fun handle(handler: HandshakePacketHandler) = handler.handleHandshake(this)
}