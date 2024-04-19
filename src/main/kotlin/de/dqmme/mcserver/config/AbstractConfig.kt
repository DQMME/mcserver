package de.dqmme.mcserver.config

import net.axay.kspigot.main.KSpigotMainInstance
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

abstract class AbstractConfig(fileName: String) {
    private val configFile = File(KSpigotMainInstance.dataFolder, fileName)
    var yamlConfiguration: YamlConfiguration

    init {
        //Save file from ressource if not exists
        if (!configFile.exists()) {
            KSpigotMainInstance.saveResource(fileName, false)
        }

        yamlConfiguration = YamlConfiguration.loadConfiguration(configFile)
    }

    fun save() {
        yamlConfiguration.save(configFile)
    }
}