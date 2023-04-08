package xyz.nkomarn.prismarine.protocol

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import xyz.nkomarn.prismarine.network.connection.ConnectionState
import xyz.nkomarn.prismarine.protocol.handshake.ServerboundHandshakePacket
import xyz.nkomarn.prismarine.protocol.login.ClientboundLoginSuccessPacket
import xyz.nkomarn.prismarine.protocol.login.ServerboundLoginRequestPacket
import xyz.nkomarn.prismarine.protocol.status.ClientboundPongPacket
import xyz.nkomarn.prismarine.protocol.status.ClientboundStatusResponsePacket
import xyz.nkomarn.prismarine.protocol.status.ServerboundPingPacket
import xyz.nkomarn.prismarine.protocol.status.ServerboundStatusRequestPacket
import kotlin.reflect.KClass

object Protocol {
    private val PACKET_IDS = mutableMapOf<KClass<*>, Int>()

    fun registerAll() {
        println("Registering all packets..")
        register(0x00, ServerboundHandshakePacket::class, FlowDirection.SERVERBOUND, ConnectionState.HANDSHAKING)
        register(0x00, ServerboundStatusRequestPacket::class, FlowDirection.SERVERBOUND, ConnectionState.STATUS)
        register(0x00, ClientboundStatusResponsePacket::class, FlowDirection.CLIENTBOUND, ConnectionState.STATUS)
        register(0x01, ClientboundPongPacket::class, FlowDirection.CLIENTBOUND, ConnectionState.STATUS)
        register(0x01, ServerboundPingPacket::class, FlowDirection.SERVERBOUND, ConnectionState.STATUS)
        register(0x00, ServerboundLoginRequestPacket::class, FlowDirection.SERVERBOUND, ConnectionState.LOGIN)
        register(0x02, ClientboundLoginSuccessPacket::class, FlowDirection.CLIENTBOUND, ConnectionState.LOGIN)
    }

    fun getPacketId(packet: Any): Int {
        return PACKET_IDS[packet::class] ?: error("Packet $packet is not registered.")
    }

    @OptIn(InternalSerializationApi::class)
    private fun register(
        id: Int,
        packet: KClass<*>,
        direction: FlowDirection,
        vararg states: ConnectionState
    ) {
        PACKET_IDS[packet] = id
        states.forEach { it.registerSerializer(id, direction, packet.serializer()) }
    }
}