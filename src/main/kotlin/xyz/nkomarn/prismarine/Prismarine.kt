package xyz.nkomarn.prismarine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import xyz.nkomarn.prismarine.network.InboundConnectionListener
import xyz.nkomarn.prismarine.protocol.Protocol

class Prismarine : CoroutineScope {

    override val coroutineContext = Dispatchers.IO + SupervisorJob()
    private val connectionListener = InboundConnectionListener()

    init {
        Protocol.registerAll()
        connectionListener.bind(25565)
    }
}