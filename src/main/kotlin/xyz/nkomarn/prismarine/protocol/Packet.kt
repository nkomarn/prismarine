package xyz.nkomarn.prismarine.protocol

import xyz.nkomarn.prismarine.protocol.handler.ServerboundPacketHandler

interface Packet

interface ServerboundPacket<T : ServerboundPacketHandler> {
    fun handle(handler: T)
}