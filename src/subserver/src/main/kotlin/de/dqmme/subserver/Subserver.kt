package de.dqmme.subserver

import de.dqmme.subserver.api.PluginMessaging
import de.dqmme.subserver.command.InstallCommand
import de.dqmme.subserver.command.LobbyCommand
import de.dqmme.subserver.command.SetBUtilsLicenseCommand
import net.axay.kspigot.main.KSpigot

class Subserver : KSpigot() {
    override fun load() {
        server.commandMap.register("lobby", LobbyCommand())
        server.commandMap.register("installsubserverplugin", InstallCommand())
        server.commandMap.register("setbutilslicense", SetBUtilsLicenseCommand())

        PluginMessaging.registerMessageChannel()
    }

    override fun shutdown() {
        PluginMessaging.unregisterMessageChannel()
    }
}