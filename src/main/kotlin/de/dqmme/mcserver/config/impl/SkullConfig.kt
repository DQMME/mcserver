package de.dqmme.mcserver.config.impl

import de.dqmme.mcserver.config.AbstractConfig

lateinit var skullConfig: SkullConfig

class SkullConfig : AbstractConfig("skulls.yml") {
    fun getSkullBase64(key: String) = yamlConfiguration.getString(key) ?: ""
}