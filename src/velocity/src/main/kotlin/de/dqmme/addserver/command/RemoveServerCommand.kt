package de.dqmme.addserver.command

import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.proxy.ConsoleCommandSource
import com.velocitypowered.api.proxy.ProxyServer
import de.dqmme.addserver.util.ServersFile
import net.kyori.adventure.text.Component


class RemoveServerCommand(private val proxyServer: ProxyServer) : SimpleCommand {
    override fun execute(command: SimpleCommand.Invocation) {
        if(command.source() !is ConsoleCommandSource) {
            command.source().sendMessage(Component.text("Der Command kann nur als Konsole ausgeführt werden"))
            return
        }

        if(command.arguments().size != 1) {
            command.source().sendMessage(Component.text("Verwendung - /removeserver <id>"))
            return
        }

        val id = command.arguments()[0].toLongOrNull()

        if(id == null) {
            command.source().sendMessage(Component.text("Verwendung - /removeserver <id>"))
            return
        }

        ServersFile.removeServer(proxyServer, id)
        command.source().sendMessage(Component.text("Removing server $id"))
    }
}