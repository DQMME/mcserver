package de.dqmme.mcserver.event

import de.dqmme.mcserver.api.PterodactylAPI
import de.dqmme.mcserver.config.impl.getLanguage
import de.dqmme.mcserver.gui.admingui.button.manageserver.inputLink
import de.dqmme.mcserver.gui.admingui.button.manageserver.inputName
import de.dqmme.mcserver.gui.admingui.button.manageserver.runCommand
import de.dqmme.mcserver.gui.admingui.scope
import io.papermc.paper.event.player.AsyncChatEvent
import kotlinx.coroutines.launch
import net.axay.kspigot.event.listen
import net.axay.kspigot.extensions.bukkit.plainText

fun registerAsyncChatEvent() = listen<AsyncChatEvent> {
    it.sendCommand()
    it.addOwnPluginInputLink()
}

private fun AsyncChatEvent.sendCommand() {
    val serverId = runCommand[player.uniqueId] ?: return

    runCommand.remove(player.uniqueId)

    val command = message().plainText()

    scope.launch {
        if(PterodactylAPI.sendCommand(serverId, command)) {
            player.sendMessage(getLanguage("server_command_sent"))
        } else {
            player.sendMessage(getLanguage("server_command_failed"))
        }
    }

    isCancelled = true
}

private fun AsyncChatEvent.addOwnPluginInputLink() {
    val server = inputLink[player.uniqueId]
    val serverLink = inputName[player.uniqueId]

    if(server != null && serverLink == null) {
        inputLink.remove(player.uniqueId)

        val link = message().plainText()

        inputName[player.uniqueId] = server to link

        player.sendMessage(getLanguage("enter_plugin_name"))
        isCancelled = true
        return
    }

    if(server == null && serverLink != null) {
        inputName.remove(player.uniqueId)

        var name = message().plainText()

        if(!name.endsWith(".jar")) name = "$name.jar"

        scope.launch {
            if(!serverLink.first.installPlugin(serverLink.second, name)) {
                player.sendMessage(getLanguage("server_command_failed"))
                return@launch
            }

            player.sendMessage(getLanguage("install_custom_plugin_command_send"))
        }
        isCancelled = true
    }
}