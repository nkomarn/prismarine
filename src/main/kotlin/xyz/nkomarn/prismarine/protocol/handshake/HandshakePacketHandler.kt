package xyz.nkomarn.prismarine.protocol.handshake

import xyz.nkomarn.prismarine.protocol.handler.ServerboundPacketHandler

interface HandshakePacketHandler : ServerboundPacketHandler {
    fun handleHandshake(packet: ServerboundHandshakePacket)
}