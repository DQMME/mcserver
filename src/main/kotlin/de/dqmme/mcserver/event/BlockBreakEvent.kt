package de.dqmme.mcserver.event

import de.dqmme.mcserver.util.Permissions
import net.axay.kspigot.event.listen
import org.bukkit.event.block.BlockBreakEvent

fun registerBlockBreakEvent() = listen<BlockBreakEvent> {
    if (it.player.hasPermission(Permissions.BREAK_BLOCKS)) return@listen

    it.isCancelled = true
}