package xyz.nkomarn.prismarine.protocol.handler

import xyz.nkomarn.prismarine.protocol.packet.ServerboundHandshakePacket
import xyz.nkomarn.prismarine.protocol.packet.ServerboundLoginPacket
import xyz.nkomarn.prismarine.protocol.packet.ServerboundPingPacket
import xyz.nkomarn.prismarine.protocol.packet.ServerboundStatusRequestPacket

interface PacketHandler {
    fun handleHandshake(packet: ServerboundHandshakePacket) {}
    fun handleStatusRequest(packet: ServerboundStatusRequestPacket) {}
    fun handlePingRequest(packet: ServerboundPingPacket) {}
    fun handleLogin(packet: ServerboundLoginPacket) {}
}