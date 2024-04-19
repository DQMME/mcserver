package de.dqmme.mcserver.config.impl

import de.dqmme.mcserver.config.AbstractConfig
import de.dqmme.mcserver.dataclass.NavigatorItem
import org.bukkit.Material

lateinit var navigatorConfig: NavigatorConfig

class NavigatorConfig : AbstractConfig("navigator.yml") {
    fun getServerItem(serverId: Long): NavigatorItem? {
        val row = yamlConfiguration.getString("$serverId.row")?.toIntOrNull()
        val slotInRow = yamlConfiguration.getString("$serverId.slot_in_row")?.toIntOrNull()
        val name = yamlConfiguration.getString("$serverId.name")
        val material = yamlConfiguration.getString("$serverId.material")?.getMaterial()

        if (row == null || slotInRow == null || name == null || material == null) return null

        return NavigatorItem(row, slotInRow, name, material)
    }

    fun setNavigatorItem(serverId: Long, navigatorItem: NavigatorItem) {
        yamlConfiguration.set("$serverId.row", navigatorItem.row)
        yamlConfiguration.set("$serverId.slot_in_row", navigatorItem.slotInRow)
        yamlConfiguration.set("$serverId.name", navigatorItem.name)
        yamlConfiguration.set("$serverId.material", navigatorItem.material.name)
        save()
    }

    fun deleteNavigatorItem(serverId: Long) {
        yamlConfiguration.set("$serverId.row", null)
        yamlConfiguration.set("$serverId.slot_in_row", null)
        yamlConfiguration.set("$serverId.name", null)
        yamlConfiguration.set("$serverId.material", null)
        save()
    }

    private fun String.getMaterial(): Material? {
        return Material.getMaterial(this)
    }
}