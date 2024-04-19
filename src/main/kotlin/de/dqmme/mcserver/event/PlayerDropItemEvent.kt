package de.dqmme.mcserver.event

import de.dqmme.mcserver.util.Permissions
import net.axay.kspigot.event.listen
import org.bukkit.event.player.PlayerDropItemEvent

fun registerPlayerDropItemEvent() = listen<PlayerDropItemEvent> {
    if (it.player.hasPermission(Permissions.DROP_ITEMS)) return@listen

    it.isCancelled = true
}