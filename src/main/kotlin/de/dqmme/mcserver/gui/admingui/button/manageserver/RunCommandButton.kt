package de.dqmme.mcserver.gui.admingui.button.manageserver

import com.mattmalec.pterodactyl4j.client.entities.ClientServer
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

val runCommand = hashMapOf<UUID, String>()

fun GUIPageBuilder<ForInventoryFiveByNine>.runCommandButton(
    slots: InventorySlotCompound<ForInventoryFiveByNine>,
    material: Material,
    serverInfo: ClientServer
) {
    button(slots, itemStack(material) {
        meta {
            name = "<green>Command ausführen".deserializeMini()

            setLore {
                +"<green>Führe einen Command auf dem Server aus".deserializeMini()
            }
        }
    }) {
        runCommand[it.player.uniqueId] = serverInfo.identifier
        it.player.closeInventory()
    }
}