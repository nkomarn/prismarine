package xyz.nkomarn.prismarine.network.connection

import kotlinx.serialization.KSerializer
import xyz.nkomarn.prismarine.protocol.FlowDirection

enum class ConnectionState {
    HANDSHAKING,
    STATUS,
    LOGIN,
    PLAY;

    private val packets = mutableMapOf<FlowDirection, Array<KSerializer<*>?>>()

    fun registerSerializer(id: Int, direction: FlowDirection, serializer: KSerializer<*>) {
        packets.computeIfAbsent(direction) { arrayOfNulls(255) }[id] = serializer
    }

    fun getSerializer(id: Int, direction: FlowDirection): KSerializer<*>? {
        return packets[direction]?.get(id)
    }
}