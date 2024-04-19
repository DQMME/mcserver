package de.dqmme.mcserver.event

import de.dqmme.mcserver.util.Permissions
import net.axay.kspigot.event.listen
import org.bukkit.event.block.BlockPlaceEvent

fun registerBlockPlaceEvent() = listen<BlockPlaceEvent> {
    if (it.player.hasPermission(Permissions.PLACE_BLOCKS)) return@listen

    it.isCancelled = true
}