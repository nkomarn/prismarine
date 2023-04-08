package xyz.nkomarn.prismarine.protocol.status

import xyz.nkomarn.prismarine.protocol.handler.ServerboundPacketHandler

interface StatusPacketHandler : ServerboundPacketHandler {
    fun handleStatusRequest(packet: ServerboundStatusRequestPacket)
    fun handlePing(packet: ServerboundPingPacket)
}