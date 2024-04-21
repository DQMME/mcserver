package de.dqmme.mcserver.gui.admingui.button.manageserver

import com.mattmalec.pterodactyl4j.client.entities.ClientServer
import com.mattmalec.pterodactyl4j.client.entities.Utilization
import de.dqmme.mcserver.gui.admingui.AdminGUIItems
import net.axay.kspigot.gui.ForInventoryFiveByNine
import net.axay.kspigot.gui.GUIPageBuilder
import net.axay.kspigot.gui.InventorySlotCompound

fun GUIPageBuilder<ForInventoryFiveByNine>.serverInfoPlaceholder(
    slot: InventorySlotCompound<ForInventoryFiveByNine>,
    serverInfo: ClientServer,
    utilization: Utilization?
) {
    placeholder(slot, AdminGUIItems.serverInfoItem(serverInfo, utilization))
}