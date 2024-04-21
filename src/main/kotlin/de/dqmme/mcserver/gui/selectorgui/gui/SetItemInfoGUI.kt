package de.dqmme.mcserver.gui.selectorgui.gui

import de.dqmme.mcserver.dataclass.Server
import de.dqmme.mcserver.gui.selectorgui.page.setiteminfo.setMaterialPage
import de.dqmme.mcserver.gui.selectorgui.page.setiteminfo.setServerPage
import de.dqmme.mcserver.gui.selectorgui.page.setiteminfo.startPage
import de.dqmme.mcserver.database.Database
import de.dqmme.mcserver.util.deserializeMini
import net.axay.kspigot.gui.GUIType
import net.axay.kspigot.gui.InventorySlot
import net.axay.kspigot.gui.PageChangeEffect
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.gui.kSpigotGUI
import net.axay.kspigot.gui.openGUI
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.setLore
import net.axay.kspigot.runnables.task
import org.bukkit.Material
import org.bukkit.entity.Player

fun Player.openSetInfoGUI(
    servers: List<Server>,
    slot: InventorySlot,
    setName: String? = null,
    setMaterial: Material? = null,
    setServer: Server? = null,
    searchQuery: String? = null
) {
    task(true) {
        openGUI(kSpigotGUI(GUIType.THREE_BY_NINE) {
            val openSelectItem = searchQuery != null

            startPage(servers, slot, setName, setMaterial, setServer, openSelectItem)

            setMaterialPage(servers, slot, setName, setMaterial, setServer, searchQuery, openSelectItem)

            setServerPage(servers, slot, setName, setMaterial, openSelectItem)

            page(4) {
                transitionTo = PageChangeEffect.SLIDE_VERTICALLY
                transitionFrom = PageChangeEffect.SLIDE_VERTICALLY

                placeholder(Slots.All, itemStack(Material.GRAY_STAINED_GLASS_PANE) {
                    meta {
                        name = "<gray>Warte...".deserializeMini()

                        setLore {
                            +"<gray>Das Item wird gespeichert".deserializeMini()
                        }
                    }
                })
            }
        })
    }
}

suspend fun Player.openReloadedSetInfoGUI(
    slot: InventorySlot,
    setName: String? = null,
    setMaterial: Material? = null,
    setServer: Server? = null,
    searchQuery: String? = null
) {
    val servers = Database.getServers()

    openSetInfoGUI(servers, slot, setName, setMaterial, setServer, searchQuery)
}