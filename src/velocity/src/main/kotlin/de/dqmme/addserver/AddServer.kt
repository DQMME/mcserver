package de.dqmme.addserver

import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import de.dqmme.addserver.command.AddServerCommand
import de.dqmme.addserver.command.RemoveServerCommand
import de.dqmme.addserver.util.ServersFile
import de.dqmme.addserver.util.registerServer
import org.slf4j.Logger
import java.nio.file.Path

@Plugin(
    id = "addserver",
    name = "AddServer",
    version = "1.0",
    dependencies = [],
    url = "https://github.com/mcserver",
    description = "Add Servers via command",
    authors = [ "DQMME" ]
)
class AddServer {
    @Inject
    lateinit var log: Logger

    @Inject
    lateinit var proxy: ProxyServer

    @Inject
    @DataDirectory
    lateinit var dataFolderPath: Path

    @Subscribe
    fun onStart(event: ProxyInitializeEvent) {
        ServersFile(dataFolderPath)

        proxy.commandManager.register("addserver", AddServerCommand(proxy))
        proxy.commandManager.register("removeserver", RemoveServerCommand(proxy))

        ServersFile.getServers().forEach {
            proxy.registerServer(it)
        }
    }
}