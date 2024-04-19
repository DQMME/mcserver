package de.dqmme.subserver

import de.dqmme.subserver.command.InstallCommand
import net.axay.kspigot.main.KSpigot

class Subserver : KSpigot() {
    override fun load() {
        server.commandMap.register("installsubserverplugin", InstallCommand())
    }
}