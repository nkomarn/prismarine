package xyz.nkomarn.prismarine.protocol.login

import xyz.nkomarn.prismarine.protocol.handler.ServerboundPacketHandler

interface LoginPacketHandler : ServerboundPacketHandler {
    fun handleLoginRequest(packet: ServerboundLoginRequestPacket)
}