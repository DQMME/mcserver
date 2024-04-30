package de.dqmme.subserver.command

import de.dqmme.subserver.api.PluginMessaging
import org.bukkit.command.CommandSender
import org.bukkit.command.defaults.BukkitCommand
import org.bukkit.entity.Player

class LobbyCommand : BukkitCommand(
    "lobby", "Sendet dich zum Lobby server", "/lobby", listOf("hub", "l")
) {
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>?): Boolean {
        if(sender !is Player) {
            sender.sendMessage("§cDas kann nur in der Konsole ausgeführt werden.")
            return true
        }

        PluginMessaging.sendToServer(sender, "lobby")

        return true
    }
}