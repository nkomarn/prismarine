@file:UseSerializers(
    JsonComponentSerializer::class,
    UUIDSerializer::class,
)

package xyz.nkomarn.prismarine.protocol.status

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import net.kyori.adventure.text.Component
import xyz.nkomarn.prismarine.codec.JsonComponentSerializer
import xyz.nkomarn.prismarine.codec.UUIDSerializer
import java.util.*

/* todo - is there some annotation to serialize this as json automatically?? */
@Serializable
data class ServerStatus(
    val description: Component,
    val version: Version,
    val players: Players,
    val favicon: String?,
    val enforcesSecureChat: Boolean,
) {
    @Serializable
    data class Version(val name: String, val protocol: Int)

    @Serializable
    data class Players(
        val online: Int,
        val max: Int,
        val sample: List<Sample>,
    ) {
        @Serializable
        data class Sample(val name: String, val id: UUID)
    }
}