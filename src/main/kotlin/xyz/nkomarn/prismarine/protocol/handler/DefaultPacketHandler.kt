package xyz.nkomarn.prismarine.protocol.handler

import net.kyori.adventure.text.Component.space
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import xyz.nkomarn.prismarine.protocol.ServerStatus
import xyz.nkomarn.prismarine.protocol.ServerStatus.Players
import xyz.nkomarn.prismarine.protocol.ServerStatus.Players.Sample
import xyz.nkomarn.prismarine.protocol.ServerStatus.Version
import xyz.nkomarn.prismarine.protocol.connection.Connection
import xyz.nkomarn.prismarine.protocol.connection.ConnectionState
import xyz.nkomarn.prismarine.protocol.packet.*
import java.util.*

class DefaultPacketHandler(private val connection: Connection) : PacketHandler {

    override fun handleHandshake(packet: ServerboundHandshakePacket) {
        println("Received handshake w/ version ${packet.protocolVersion}. Next state: ${packet.nextState}")

        /* todo - verify that this is either status or login, nothing else */
        connection.state = packet.nextState
    }

    override fun handleStatusRequest(packet: ServerboundStatusRequestPacket) {
        val sample = listOf(
            Sample("kytachips", UUID.randomUUID()),
            Sample("viztea", UUID.randomUUID()),
        )

        connection.send(
            ClientboundStatusResponsePacket(
                ServerStatus(
                    text()
                        .append(text("greetings from"))
                        .append(space())
                        .append(text("prismarine!", NamedTextColor.AQUA))
                        .build(),
                    Version("1.19.4", 762),
                    Players(sample.size, 20, sample),
                    null,
                    true
                )
            )
        )
    }

    override fun handlePingRequest(packet: ServerboundPingPacket) {
        connection.send(ClientboundPingPacket(packet.timestamp))
    }

    override fun handleLogin(packet: ServerboundLoginPacket) {
        connection.send(ClientboundLoginSuccessPacket(packet.uuid ?: UUID.randomUUID(), packet.username, 0))
        connection.state = ConnectionState.PLAY
    }
}