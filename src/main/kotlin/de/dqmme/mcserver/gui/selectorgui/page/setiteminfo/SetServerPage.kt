package de.dqmme.mcserver.gui.selectorgui.page.setiteminfo

import de.dqmme.mcserver.config.impl.navigatorConfig
import de.dqmme.mcserver.dataclass.Server
import de.dqmme.mcserver.gui.GUIItems
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
import org.bukkit.inventory.ItemStack

fun GUIBuilder<ForInventoryThreeByNine>.setServerPage(
    servers: List<Server>,
    slot: InventorySlot,
    setName: String?,
    setMaterial: Material?,
    openSelectItem: Boolean
) {
    page(3) {
        transitionTo = PageChangeEffect.SLIDE_VERTICALLY
        transitionFrom = PageChangeEffect.SLIDE_VERTICALLY

        val compound = createRectCompound<Server>(
            Slots.RowOneSlotTwo, Slots.RowThreeSlotEight,
            iconGenerator = { serverItem(it) },
            onClick = { clickEvent, element ->
                clickEvent.player.openSetInfoGUI(servers, slot, setName, setMaterial, element, null)

                clickEvent.bukkitEvent.isCancelled = true
            })

        compound.sortContentBy { it.name }

        compound.addContent(servers)

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
    }
}

private fun serverItem(server: Server): ItemStack {
    if(navigatorConfig.getServerItem(server.id) != null) {
        return itemStack(Material.YELLOW_DYE) {
            meta {
                name = "<yellow>${server.name}".deserializeMini()

                setLore {
                    +"<yellow><bold>Zu diesem Server existiert bereits ein Item, dieses wird ersetzt".deserializeMini()
                    +"<green>Setze <yellow>${server.name} <green>als Server".deserializeMini()
                }
            }
        }
    }

    return itemStack(Material.LIME_DYE) {
        meta {
            name = "<green>${server.name}".deserializeMini()

            setLore {
                +"<green>Setze <yellow>${server.name} <green>als Server".deserializeMini()
            }
        }
    }
}