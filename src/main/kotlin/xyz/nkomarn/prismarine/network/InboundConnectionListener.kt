package xyz.nkomarn.prismarine.network

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.*
import xyz.nkomarn.prismarine.protocol.FlowDirection
import xyz.nkomarn.prismarine.protocol.encoding.MinecraftPacketDecoder
import xyz.nkomarn.prismarine.protocol.encoding.readVarInt
import xyz.nkomarn.prismarine.network.connection.Connection
import xyz.nkomarn.prismarine.network.connection.ConnectionState
import xyz.nkomarn.prismarine.protocol.ServerboundPacket
import xyz.nkomarn.prismarine.protocol.handler.ServerboundPacketHandler

class InboundConnectionListener : CoroutineScope {
    override val coroutineContext = Dispatchers.IO + SupervisorJob()
    private val selectorManager = SelectorManager(Dispatchers.IO)

    fun bind(port: Int) {
        val serverSocket = aSocket(selectorManager)
            .tcp()
            .tcpNoDelay()
            .bind("0.0.0.0", port)

        println("Server is listening at ${serverSocket.localAddress}")

        runBlocking {
            while (true) {
                val socket = serverSocket.accept()
                val readChannel = socket.openReadChannel()
                val writeChannel = socket.openWriteChannel(autoFlush = true)
                val connection = Connection(ConnectionState.HANDSHAKING, socket, readChannel, writeChannel)

                println("Accepted incoming connection: ${socket.localAddress}")
                listenForPackets(connection)
            }
        }
    }

    private fun listenForPackets(connection: Connection) {
        launch {
            val readChannel = connection.readChannel

            while (true) {
                readChannel.awaitContent()

                if (readChannel.isClosedForRead) {
                    break
                }

                val length = readChannel.readVarInt()
                val data = readChannel.readPacket(length)
                val packetId = data.readVarInt()
                val serializer = connection.state.getSerializer(packetId, FlowDirection.SERVERBOUND)

                if (serializer == null) {
                    println("No serializer present for packet #$packetId, skipping..")
                    continue
                }

                val decoder = MinecraftPacketDecoder(data)
                val packet = serializer.deserialize(decoder)
                    ?.let { it as ServerboundPacket<ServerboundPacketHandler> }
                    ?: continue

                println("Read packet #$packetId [$length bytes]: $packet")
                packet.handle(connection.packetHandler)
            }

            println("Connection closed.")
        }
    }
}