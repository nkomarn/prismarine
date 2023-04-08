package xyz.nkomarn.prismarine.network.connection

import io.ktor.network.sockets.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import xyz.nkomarn.prismarine.protocol.FlowDirection
import xyz.nkomarn.prismarine.protocol.Protocol
import xyz.nkomarn.prismarine.protocol.encoding.MinecraftPacketEncoder
import xyz.nkomarn.prismarine.protocol.encoding.writeVarInt
import xyz.nkomarn.prismarine.protocol.Packet
import xyz.nkomarn.prismarine.protocol.handler.ServerboundPacketHandler
import xyz.nkomarn.prismarine.protocol.handler.VanillaHandshakePacketHandler

data class Connection(
    var state: ConnectionState,
    val socket: Socket,
    val readChannel: ByteReadChannel,
    val writeChannel: ByteWriteChannel,
) {
    var packetHandler: ServerboundPacketHandler = VanillaHandshakePacketHandler(this)
    val isOpen: Boolean
        get() = !readChannel.isClosedForRead

    fun disconnect() {
        socket.close()
    }

    fun <T : Packet> send(packet: T) {
        val packetId = Protocol.getPacketId(packet)
        println("Sending packet #$packetId: $packet")

        val serializer = state.getSerializer(packetId, FlowDirection.CLIENTBOUND)
            ?.let { it as KSerializer<T> }
            ?: return println("No serializer present for packet #$packetId, skipping..")

        val data = BytePacketBuilder().also { it.writeVarInt(packetId) }
        val encoder = MinecraftPacketEncoder(data)
        encoder.encodeSerializableValue(serializer, packet)

        GlobalScope.launch { /* todo - use dedicated network scope */
            writeChannel.writeVarInt(data.size)
            writeChannel.writeFully(data.build().readBytes())
        }
    }
}
