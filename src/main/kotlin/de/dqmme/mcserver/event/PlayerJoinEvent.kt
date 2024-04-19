package de.dqmme.mcserver.event

import de.dqmme.mcserver.config.impl.pluginConfig
import de.dqmme.mcserver.item.Items
import de.dqmme.mcserver.util.Permissions
import net.axay.kspigot.event.listen
import org.bukkit.event.player.PlayerJoinEvent

fun registerPlayerJoinEvent() = listen<PlayerJoinEvent> {
    //Disable join message
    it.joinMessage(null)

    //Clear player inventory and give compass
    it.player.inventory.clear()
    if (it.player.hasPermission(Permissions.ADMIN_PERMISSION)) it.player.inventory.setItem(8, Items.adminItem)
    it.player.inventory.setItem(4, Items.selectorItem)

    //Set health and saturation to 20
    it.player.health = 20.0
    it.player.foodLevel = 20

    //Teleport player to spawn location
    val spawnLocation = pluginConfig.getSpawnLocation()
    if (spawnLocation != null) it.player.teleport(spawnLocation)
}