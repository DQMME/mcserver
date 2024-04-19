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

fun GUIPageBuilder<ForInventoryThreeByNine>.setNameButton(
    servers: List<Server>,
    slot: InventorySlotCompound<ForInventoryThreeByNine>,
    setSlot: InventorySlot,
    setName: String? = null,
    setMaterial: Material?,
    setServer: Server? = null
) {
    button(slot, itemStack(Material.NAME_TAG) {
        meta {
            name = "<green>Namen setzen".deserializeMini()

            setLore {
                +"<green>Setze den Namen des Items".deserializeMini()
                if (setName != null) +"<yellow>Aktuell: <aqua>$setName".deserializeMini()
            }
        }
    }) {
        SignGUI.builder()
            .setHandler { player, signGUIResult ->
                var name: String? = signGUIResult.lines.joinToString("")

                if (name?.isEmpty() == true) name = null

                player.openSetInfoGUI(servers, setSlot, name, setMaterial, setServer, null)

                listOf(SignGUIAction.run { })
            }
            .build()
            .open(it.player)
    }
}