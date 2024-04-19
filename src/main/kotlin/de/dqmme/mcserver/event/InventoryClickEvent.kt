package de.dqmme.mcserver.event

import de.dqmme.mcserver.util.Permissions
import net.axay.kspigot.event.listen
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent

fun registerInventoryClickEvent() = listen<InventoryClickEvent> {
    if (it.whoClicked.hasPermission(Permissions.MOVE_ITEMS)) return@listen

    if (it.action != InventoryAction.MOVE_TO_OTHER_INVENTORY &&
        it.action != InventoryAction.HOTBAR_SWAP &&
        it.action != InventoryAction.SWAP_WITH_CURSOR &&
        it.action != InventoryAction.DROP_ALL_SLOT &&
        it.action != InventoryAction.DROP_ALL_CURSOR &&
        it.action != InventoryAction.PICKUP_ALL &&
        it.action != InventoryAction.PLACE_ALL
    ) return@listen

    it.isCancelled = true
}