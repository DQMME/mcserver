package de.dqmme.mcserver.config.impl

import de.dqmme.mcserver.config.AbstractConfig
import de.dqmme.mcserver.dataclass.Rank

lateinit var rankConfig: RankConfig

class RankConfig : AbstractConfig("ranks.yml") {
    fun getRanks(): List<Rank> {
        val ranks = mutableListOf<Rank>()

        yamlConfiguration.getKeys(false).forEach {
            val id = it
            val permission = yamlConfiguration.getString("$it.permission")
            val priority = yamlConfiguration.getInt("$it.priority")
            val prefix = yamlConfiguration.getString("$it.prefix")
            val suffix = yamlConfiguration.getString("$it.suffix")

            ranks.add(Rank(id, priority, permission, prefix, suffix))
        }

        return ranks.toList()
    }
}