package de.dqmme.mcserver.config.impl

import de.dqmme.mcserver.config.AbstractConfig
import de.dqmme.mcserver.util.deserializeMini

lateinit var skullConfig: SkullConfig

class SkullConfig : AbstractConfig("skulls.yml") {
    fun getSkullName(key: String) = yamlConfiguration.getString("$key.name")?.deserializeMini() ?: "".deserializeMini()

    fun getSkullBase64(key: String) = yamlConfiguration.getString("$key.base64") ?: ""
}