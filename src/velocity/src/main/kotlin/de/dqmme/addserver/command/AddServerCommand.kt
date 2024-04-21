package de.dqmme.addserver.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.ConsoleCommandSource
import com.velocitypowered.api.proxy.ProxyServer
import de.dqmme.addserver.dataclass.Server
import de.dqmme.addserver.util.ServersFile
import net.kyori.adventure.text.Component


class AddServerCommand(private val proxyServer: ProxyServer) : SimpleCommand {
    override fun execute(command: SimpleCommand.Invocation) {
        if (command.source() !is ConsoleCommandSource) {
            command.source().sendMessage(Component.text("Der Command kann nur als Konsole ausgeführt werden"))
            return
        }

        if (command.arguments().size != 3) {
            command.source().sendMessage(Component.text("Verwendung - /addserver <id> <ip> <port>"))
            return
        }

        val id = command.arguments()[0].toLongOrNull()
        val ip = command.arguments()[1]
        val port = command.arguments()[2].toIntOrNull()

        if (id == null || ip.isEmpty() || port == null) {
            command.source().sendMessage(Component.text("Verwendung - /addserver <id> <ip> <port>"))
            return
        }

        ServersFile.saveServer(proxyServer, Server(id, ip, port))
        command.source().sendMessage(Component.text("Adding server $id ($ip, $port)"))
    }
}