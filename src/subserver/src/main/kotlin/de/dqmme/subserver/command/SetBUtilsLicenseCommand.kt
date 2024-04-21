package de.dqmme.subserver.command

import net.axay.kspigot.main.KSpigotMainInstance
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.command.defaults.BukkitCommand
import java.io.File

class SetBUtilsLicenseCommand : BukkitCommand("setbutilslicense") {
    override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
        if (sender !is ConsoleCommandSender) {
            sender.sendMessage("Dieser Command kann nur von der Konsole ausgeführt werden.")
            return true
        }

        if (args.size != 1) {
            sender.sendMessage("Verwendung: /setbutilslicense <License>")
            return true
        }

        val license = args[0]

        val bUtilsFolder = File(KSpigotMainInstance.dataFolder.parentFile, "BUtils")

        if (!bUtilsFolder.exists() || !bUtilsFolder.isDirectory) {
            bUtilsFolder.mkdir()
        }

        val bUtilsConfigFile = File(bUtilsFolder, "config.yml")

        if(!bUtilsConfigFile.exists()) {
            bUtilsConfigFile.createNewFile()

            bUtilsConfigFile.writeText("license: '$license'")
            sender.sendMessage("BUtils license has been set")
            return true
        }

        sender.sendMessage("Trying to set license (works only if license is not set)")

        val fileText = bUtilsConfigFile.readText()

        if(fileText.contains("license")) {
            bUtilsConfigFile.writeText(fileText.replace("license: ''", "license: '$license'"))
        } else {
            bUtilsConfigFile.writeText("$fileText\nlicense: '$license'")
        }

        return true
    }
}