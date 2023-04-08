package xyz.nkomarn.prismarine.protocol.handler

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import xyz.nkomarn.prismarine.network.connection.Connection
import xyz.nkomarn.prismarine.network.connection.ConnectionState
import xyz.nkomarn.prismarine.protocol.handshake.HandshakePacketHandler
import xyz.nkomarn.prismarine.protocol.handshake.ServerboundHandshakePacket
import xyz.nkomarn.prismarine.protocol.login.ClientboundLoginSuccessPacket
import xyz.nkomarn.prismarine.protocol.login.LoginPacketHandler
import xyz.nkomarn.prismarine.protocol.login.ServerboundLoginRequestPacket
import xyz.nkomarn.prismarine.protocol.status.*
import java.util.*

class VanillaHandshakePacketHandler(private val connection: Connection) : HandshakePacketHandler {
    override fun handleHandshake(packet: ServerboundHandshakePacket) {
        when (packet.nextState) {
            ConnectionState.STATUS -> connection.packetHandler = VanillaStatusPacketHandler(connection)
            ConnectionState.LOGIN -> connection.packetHandler = VanillaLoginPacketHandler(connection)
            else -> return connection.disconnect()
        }

        connection.state = packet.nextState
    }
}

class VanillaStatusPacketHandler(private val connection: Connection) : StatusPacketHandler {
    override fun handleStatusRequest(packet: ServerboundStatusRequestPacket) {
        val sample = listOf(
            ServerStatus.Players.Sample("kytachips", UUID.randomUUID()),
            ServerStatus.Players.Sample("viztea", UUID.randomUUID()),
            ServerStatus.Players.Sample("ur mom", UUID.randomUUID()),
        )

        connection.send(
            ClientboundStatusResponsePacket(
                ServerStatus(
                    Component.text()
                        .append(Component.text("greetings from", TextColor.fromHexString("#99f6e4")))
                        .append(Component.space())
                        .append(Component.text("prismarine!", TextColor.fromHexString("#2dd4bf")))
                        .build(),
                    ServerStatus.Version("1.19.4", 762),
                    ServerStatus.Players(sample.size, 20, sample),
                    null,
                    true
                )
            )
        )
    }

    override fun handlePing(packet: ServerboundPingPacket) {
        connection.send(ClientboundPongPacket(packet.timestamp))
    }
}

class VanillaLoginPacketHandler(private val connection: Connection) : LoginPacketHandler {
    override fun handleLoginRequest(packet: ServerboundLoginRequestPacket) {
        connection.send(ClientboundLoginSuccessPacket(packet.uuid ?: UUID.randomUUID(), packet.username, 0))
        connection.state = ConnectionState.PLAY
    }
}