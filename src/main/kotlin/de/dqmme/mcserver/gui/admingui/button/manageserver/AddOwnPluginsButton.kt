package de.dqmme.mcserver.gui.admingui.button.manageserver

import de.dqmme.mcserver.config.impl.getLanguage
import de.dqmme.mcserver.dataclass.Server
import de.dqmme.mcserver.util.deserializeMini
import net.axay.kspigot.gui.ForInventoryFiveByNine
import net.axay.kspigot.gui.GUIPageBuilder
import net.axay.kspigot.gui.InventorySlotCompound
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.setLore
import org.bukkit.Material
import java.util.UUID

val inputLink = hashMapOf<UUID, Server>()
val inputName = hashMapOf<UUID, Pair<Server, String>>()

fun GUIPageBuilder<ForInventoryFiveByNine>.addOwnPluginsButton(
    slots: InventorySlotCompound<ForInventoryFiveByNine>,
    material: Material,
    server: Server
) {
    button(slots, itemStack(material) {
        meta {
            name = "<green>Eigene Plugins hinzufügen".deserializeMini()

            setLore {
                +"<green>Füge eigene Plugins hinzu".deserializeMini()
            }
        }
    }) {
        it.player.sendMessage(getLanguage("enter_plugin_link"))
        inputLink[it.player.uniqueId] = server
        it.player.closeInventory()
    }
}