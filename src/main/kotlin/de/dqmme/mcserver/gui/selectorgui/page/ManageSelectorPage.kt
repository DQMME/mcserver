package de.dqmme.mcserver.gui.selectorgui.page

import de.dqmme.mcserver.config.impl.navigatorConfig
import de.dqmme.mcserver.dataclass.NavigatorItem
import de.dqmme.mcserver.dataclass.Server
import de.dqmme.mcserver.gui.admingui.scope
import de.dqmme.mcserver.gui.selectorgui.gui.openReloadedSetInfoGUI
import de.dqmme.mcserver.gui.selectorgui.openSelectorGUI
import de.dqmme.mcserver.item.Skulls
import de.dqmme.mcserver.util.Database
import de.dqmme.mcserver.util.deserializeMini
import kotlinx.coroutines.launch
import net.axay.kspigot.gui.ForInventorySixByNine
import net.axay.kspigot.gui.GUIBuilder
import net.axay.kspigot.gui.InventoryDimensions
import net.axay.kspigot.gui.InventorySlot
import net.axay.kspigot.gui.PageChangeEffect
import net.axay.kspigot.gui.SingleInventorySlot
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.setLore
import org.bukkit.Material

fun GUIBuilder<ForInventorySixByNine>.manageSelectorPage(
    items: HashMap<Server, NavigatorItem>,
    startManagePage: Boolean
) {
    page(if (startManagePage) 1 else 2) {
        transitionTo = PageChangeEffect.SLIDE_VERTICALLY
        transitionFrom = PageChangeEffect.SLIDE_VERTICALLY

        button(Slots.All, itemStack(Material.LIME_DYE) {
            meta {
                name = "<green>Slot verfügbar".deserializeMini()

                setLore {
                    +"<green>Klicke, um einen Server zu setzen".deserializeMini()
                }
            }
        }) {
            val inventorySlot = InventorySlot.fromRealSlot(
                it.bukkitEvent.slot, InventoryDimensions(9, 6)
            ) ?: return@button

            scope.launch {
                it.player.openReloadedSetInfoGUI(inventorySlot)
            }
        }

        items.forEach { item ->
            pageChanger(SingleInventorySlot(item.value.row, item.value.slotInRow), itemStack(Material.RED_DYE) {
                meta {
                    name = "<red>${item.value.name} <red><bold>löschen".deserializeMini()

                    setLore {
                        +"<red>Lösche das Item aus dem Navigator".deserializeMini()
                    }
                }
            }, 3, null) {
                scope.launch {
                    navigatorConfig.deleteNavigatorItem(item.key.id)
                    Database.saveServer(item.key.copy(isOnNavigator = false))
                    it.player.openSelectorGUI(true)
                }
            }
        }

        placeholder(Slots.RowOneSlotNine, itemStack(Material.BARRIER) {
            meta {
                name = "<red>Belegt".deserializeMini()

                setLore {
                    +"<red>Hier kannst du kein Item platzieren".deserializeMini()
                }
            }
        })

        pageChanger(Slots.RowOneSlotOne, Skulls.arrowLeft, if (startManagePage) 2 else 1, null, null)
    }
}