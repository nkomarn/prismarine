package xyz.nkomarn.prismarine.protocol

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.serializer
import xyz.nkomarn.prismarine.protocol.connection.ConnectionState
import xyz.nkomarn.prismarine.protocol.packet.*
import kotlin.reflect.KClass

object Protocol {
    private val PACKET_IDS = mutableMapOf<KClass<*>, Int>()

    init {
        println("Registering all packets..")
        register(0x00, ServerboundHandshakePacket::class, FlowDirection.SERVERBOUND, ConnectionState.HANDSHAKING)
        register(0x00, ServerboundStatusRequestPacket::class, FlowDirection.SERVERBOUND, ConnectionState.STATUS)
        register(0x00, ClientboundStatusResponsePacket::class, FlowDirection.CLIENTBOUND, ConnectionState.STATUS)
        register(0x01, ClientboundPingPacket::class, FlowDirection.CLIENTBOUND, ConnectionState.STATUS)
        register(0x01, ServerboundPingPacket::class, FlowDirection.SERVERBOUND, ConnectionState.STATUS)
        register(0x00, ServerboundLoginPacket::class, FlowDirection.SERVERBOUND, ConnectionState.LOGIN)
        register(0x02, ClientboundLoginSuccessPacket::class, FlowDirection.CLIENTBOUND, ConnectionState.LOGIN)
    }

    /* todo - welp */
    fun init() {}

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