package xyz.nkomarn.prismarine.protocol.packet

import xyz.nkomarn.prismarine.protocol.handler.PacketHandler

interface Packet

interface ServerboundPacket {
    fun handle(handler: PacketHandler)
}