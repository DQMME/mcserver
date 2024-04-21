package de.dqmme.mcserver.gui.selectorgui.button.setiteminfo

import de.dqmme.mcserver.dataclass.Server
import de.dqmme.mcserver.gui.selectorgui.gui.openSetInfoGUI
import de.dqmme.mcserver.util.deserializeMini
import de.rapha149.signgui.SignGUI
import de.rapha149.signgui.SignGUIAction
import net.axay.kspigot.gui.ForInventoryThreeByNine
import net.axay.kspigot.gui.GUIPageBuilder
import net.axay.kspigot.gui.InventorySlot
import net.axay.kspigot.gui.InventorySlotCompound
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.setLore
import org.bukkit.Material

fun GUIPageBuilder<ForInventoryThreeByNine>.searchItemButton(
    servers: List<Server>,
    slot: InventorySlotCompound<ForInventoryThreeByNine>,
    setSlot: InventorySlot,
    setName: String? = null,
    setServer: Server? = null,
    setMaterial: Material?
) {
    button(slot, itemStack(Material.SPYGLASS) {
        meta {
            name = "<green>Suchen".deserializeMini()

            setLore {
                +"<green>Suche ein Item".deserializeMini()
            }
        }
    }) {
        SignGUI.builder()
            .setHandler { player, signGUIResult ->
                val query = signGUIResult.getLine(0)

                player.openSetInfoGUI(servers, setSlot, setName, setMaterial, setServer, query)

                mutableListOf(SignGUIAction.run { })
            }
            .build()
            .open(it.player)
    }
}