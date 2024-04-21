package de.dqmme.mcserver.config.impl

import de.dqmme.mcserver.config.AbstractConfig
import de.dqmme.mcserver.dataclass.ServerPlugin

lateinit var serverPluginsConfig: ServerPluginsConfig

class ServerPluginsConfig : AbstractConfig("server-plugins.yml") {
    fun getServerPlugins(): List<ServerPlugin> {
        val plugins = mutableListOf<ServerPlugin>()

        yamlConfiguration.getKeys(false).forEach {
            val link = yamlConfiguration.getString("$it.link")
            val name = yamlConfiguration.getString("$it.name")

            if (link == null || name == null) return@forEach

            plugins.add(ServerPlugin(it, link, name))
        }

        return plugins.toList()
    }
}