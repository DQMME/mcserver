package de.dqmme.mcserver.gui.admingui.button.manageserver

import de.dqmme.mcserver.dataclass.Server
import de.dqmme.mcserver.gui.admingui.scope
import de.dqmme.mcserver.util.Database
import de.dqmme.mcserver.util.deserializeMini
import kotlinx.coroutines.launch
import net.axay.kspigot.gui.ForInventoryFiveByNine
import net.axay.kspigot.gui.GUIPageBuilder
import net.axay.kspigot.gui.InventorySlotCompound
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.setLore
import org.bukkit.Material

fun GUIPageBuilder<ForInventoryFiveByNine>.setPrivateButton(
    slot: InventorySlotCompound<ForInventoryFiveByNine>,
    server: Server
) {
    var isPrivate = server.isPrivate

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

    val waitItemStack = itemStack(Material.LIGHT_GRAY_DYE) {
        meta {
            name = "<gray>Wird aktualisiert".deserializeMini()

            setLore {
                +"<gray>Das wird gerade aktualisiert, <bold>das Item lädt sich automatisch neu".deserializeMini()
            }
        }
    }

    button(slot, if (isPrivate) privateItemStack else publicItemStack) {
        if(isPrivate) {
            isPrivate = false
            it.guiInstance[slot] = publicItemStack
        } else {
            isPrivate = true
            it.guiInstance[slot] = privateItemStack
        }

        it.guiInstance[slot] = waitItemStack

        scope.launch {
            Database.saveServer(server.copy(isPrivate = isPrivate))

            if(isPrivate) {
                it.guiInstance[slot] = privateItemStack
            } else {
                it.guiInstance[slot] = publicItemStack
            }
        }
    }
}