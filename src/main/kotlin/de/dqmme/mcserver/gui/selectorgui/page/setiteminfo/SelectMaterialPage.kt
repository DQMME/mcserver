package de.dqmme.mcserver.gui.selectorgui.page.setiteminfo

import de.dqmme.mcserver.dataclass.Server
import de.dqmme.mcserver.gui.GUIItems
import de.dqmme.mcserver.gui.selectorgui.button.setiteminfo.searchItemButton
import de.dqmme.mcserver.gui.selectorgui.gui.openSetInfoGUI
import de.dqmme.mcserver.util.deserializeMini
import net.axay.kspigot.gui.ForInventoryThreeByNine
import net.axay.kspigot.gui.GUIBuilder
import net.axay.kspigot.gui.InventorySlot
import net.axay.kspigot.gui.PageChangeEffect
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.setLore
import org.bukkit.Material

fun GUIBuilder<ForInventoryThreeByNine>.setMaterialPage(
    servers: List<Server>,
    slot: InventorySlot,
    setName: String?,
    setMaterial: Material?,
    setServer: Server?,
    searchQuery: String?,
    openSelectItem: Boolean
) {
    page(if (openSelectItem) 1 else 2) {
        transitionTo = PageChangeEffect.SLIDE_VERTICALLY
        transitionFrom = PageChangeEffect.SLIDE_VERTICALLY

        val compound = createRectCompound<Material>(
            Slots.RowOneSlotTwo, Slots.RowThreeSlotEight,
            iconGenerator = {
                itemStack(it) {
                    meta {
                        name = "<yellow>${it.name}".deserializeMini()

                        setLore {
                            +"<green>Setze <yellow>${it.name} <green>als Item".deserializeMini()
                        }
                    }
                }
            },
            onClick = { clickEvent, element ->
                clickEvent.player.openSetInfoGUI(servers, slot, setName, element, setServer, null)

                clickEvent.bukkitEvent.isCancelled = true
            })

        compound.sortContentBy { it.name }

        if (searchQuery == null) {
            compound.addContent(Material.entries)
        } else {
            compound.addContent(
                Material.entries.stream()
                    .filter { it.name.lowercase().contains(searchQuery.lowercase()) && !it.isLegacy }.toList()
            )
        }

        compoundScroll(
            Slots.RowThreeSlotNine, GUIItems.scrollUp, compound, scrollTimes = 3, reverse = true
        )

        compoundScroll(
            Slots.RowOneSlotNine, GUIItems.scrollDown, compound, scrollTimes = 3
        )

        pageChanger(
            Slots.RowThreeSlotOne,
            GUIItems.back,
            if (openSelectItem) 2 else 1,
            null,
            null
        )

        searchItemButton(servers, Slots.RowOneSlotOne, slot, setName, setServer, setMaterial)
    }
}