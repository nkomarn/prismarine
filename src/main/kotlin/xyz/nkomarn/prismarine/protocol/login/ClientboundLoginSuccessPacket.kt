package xyz.nkomarn.prismarine.protocol.login

import kotlinx.serialization.Serializable
import xyz.nkomarn.prismarine.codec.BinaryUUIDSerializer
import xyz.nkomarn.prismarine.protocol.Packet
import java.util.*

@Serializable
data class ClientboundLoginSuccessPacket(
    @Serializable(with = BinaryUUIDSerializer::class)
    val uuid: UUID,
    val username: String,
    val propertyCount: Int,
    /* todo - game profile properties */
) : Packet