package de.dqmme.subserver.api

import com.google.common.io.ByteStreams
import net.axay.kspigot.main.KSpigotMainInstance
import org.bukkit.entity.Player

object PluginMessaging {
    private const val CHANNEL = "BungeeCord"

    fun registerMessageChannel() {
        KSpigotMainInstance.server.messenger.registerOutgoingPluginChannel(KSpigotMainInstance, CHANNEL)
    }

    fun unregisterMessageChannel() {
        KSpigotMainInstance.server.messenger.unregisterOutgoingPluginChannel(KSpigotMainInstance, CHANNEL)
    }

    fun sendToServer(player: Player, serverName: String) {
        val output = ByteStreams.newDataOutput()

        output.writeUTF("Connect")
        output.writeUTF(serverName)

        player.sendPluginMessage(KSpigotMainInstance, CHANNEL, output.toByteArray())
    }
}