package de.dqmme.mcserver.gui.admingui.button.createserver

import de.dqmme.mcserver.gui.admingui.gui.createPrivateServer
import de.dqmme.mcserver.util.deserializeMini
import net.axay.kspigot.gui.ForInventoryFiveByNine
import net.axay.kspigot.gui.GUIPageBuilder
import net.axay.kspigot.gui.InventorySlotCompound
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.setLore
import org.bukkit.Material

fun GUIPageBuilder<ForInventoryFiveByNine>.setPrivateButton(
    slot: InventorySlotCompound<ForInventoryFiveByNine>
) {
    val privateItemStack = itemStack(Material.ENDER_PEARL) {
        meta {
            name = "<yellow>Ist privat".deserializeMini()

            setLore {
                +"<yellow>Der Server ist privat".deserializeMini()
                +"<yellow>Spieler müssen über das Admin-Menü hinzugefügt werden".deserializeMini()
                +"<green>Klicke um den Server auf öffentlich zu setzen".deserializeMini()
            }
        }
    }

    val publicItemStack = itemStack(Material.ENDER_EYE) {
        meta {
            name = "<green>Ist öffentlich".deserializeMini()

            setLore {
                +"<green>Der Server ist öffentlich".deserializeMini()
                +"<green>Jeder Spieler kann über den Navigator beitreten".deserializeMini()
                +"<yellow>Klicke um den Server auf privat zu setzen".deserializeMini()
            }
        }
    }

    button(slot, if (createPrivateServer) privateItemStack else publicItemStack) {
        if (createPrivateServer) {
            createPrivateServer = false
            it.guiInstance[slot] = publicItemStack
        } else {
            createPrivateServer = true
            it.guiInstance[slot] = privateItemStack
        }
    }
}