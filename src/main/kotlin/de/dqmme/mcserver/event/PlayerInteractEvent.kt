package de.dqmme.mcserver.event

import de.dqmme.mcserver.gui.admingui.openAdminGUI
import de.dqmme.mcserver.gui.selectorgui.openSelectorGUI
import de.dqmme.mcserver.item.Items
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.axay.kspigot.event.listen
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

private val scope = CoroutineScope(Dispatchers.IO)

fun registerPlayerInteractEvent() = listen<PlayerInteractEvent> {
    it.onSelectorInteract()
    it.onAdminInteract()
}

private fun PlayerInteractEvent.onSelectorInteract() {
    if (action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR) return

    if (!player.inventory.itemInMainHand.isSimilar(Items.selectorItem)) return

    scope.launch {
        player.openSelectorGUI()
    }

    isCancelled = true
}

private fun PlayerInteractEvent.onAdminInteract() {
    if (action != Action.RIGHT_CLICK_BLOCK && action != Action.RIGHT_CLICK_AIR) return

    if (!player.inventory.itemInMainHand.isSimilar(Items.adminItem)) return

    scope.launch {
        player.openAdminGUI()
    }

    isCancelled = true
}