package xyz.nkomarn.prismarine.protocol.login

import kotlinx.serialization.Serializable
import xyz.nkomarn.prismarine.codec.BinaryUUIDSerializer
import xyz.nkomarn.prismarine.protocol.ServerboundPacket
import java.util.*

@Serializable
data class ServerboundLoginRequestPacket(
    val username: String,
    val hasUUID: Boolean,
    /* todo - this field is only sent when the above is true */
    @Serializable(with = BinaryUUIDSerializer::class)
    val uuid: UUID?,
) : ServerboundPacket<LoginPacketHandler> {
    override fun handle(handler: LoginPacketHandler) = handler.handleLoginRequest(this)
}