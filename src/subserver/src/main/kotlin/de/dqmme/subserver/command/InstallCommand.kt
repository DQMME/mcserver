package de.dqmme.subserver.command

import net.axay.kspigot.main.KSpigotMainInstance
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.command.defaults.BukkitCommand
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.channels.Channels

class InstallCommand : BukkitCommand("installsubserverplugin") {
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if(sender !is ConsoleCommandSender) {
            sender.sendMessage("Dieser Command kann nur von der Konsole ausgeführt werden.")
            return true
        }

        if(args.size != 2) {
            sender.sendMessage("Verwendung: /installsubservercommand <Link> <FileName>")
            return true
        }

        val link = args[0]
        val url = try {
            URL(link)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        val fileName = args[1]

        if(url == null) return true

        sender.sendMessage("Downloading $name from $link")

        HttpURLConnection.setFollowRedirects(true)

        val connection = url.openStream()

        HttpURLConnection.setFollowRedirects(true)

        val readableByteChannel = Channels.newChannel(connection)

        val fileOutputStream = FileOutputStream(File(KSpigotMainInstance.dataFolder.parentFile, fileName))

        fileOutputStream.channel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE)
        return true
    }
}