package xyz.nkomarn.prismarine.protocol.connection

import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import xyz.nkomarn.prismarine.protocol.FlowDirection
import xyz.nkomarn.prismarine.protocol.Protocol
import xyz.nkomarn.prismarine.protocol.encoding.MinecraftPacketEncoder
import xyz.nkomarn.prismarine.protocol.encoding.writeVarInt
import xyz.nkomarn.prismarine.protocol.handler.DefaultPacketHandler
import xyz.nkomarn.prismarine.protocol.packet.Packet

data class Connection(
    var state: ConnectionState,
    val readChannel: ByteReadChannel,
    val writeChannel: ByteWriteChannel,
//     val packetHandler: PacketHandler,
) {
    val packetHandler = DefaultPacketHandler(this)
    val isOpen: Boolean
        get() = !readChannel.isClosedForRead

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
