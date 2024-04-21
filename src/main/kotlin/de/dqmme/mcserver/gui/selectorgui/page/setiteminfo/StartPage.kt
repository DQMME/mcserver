package de.dqmme.mcserver.gui.selectorgui.page.setiteminfo

import de.dqmme.mcserver.config.impl.navigatorConfig
import de.dqmme.mcserver.dataclass.NavigatorItem
import de.dqmme.mcserver.dataclass.Server
import de.dqmme.mcserver.gui.GUIItems
import de.dqmme.mcserver.gui.admingui.scope
import de.dqmme.mcserver.gui.selectorgui.button.setiteminfo.setNameButton
import de.dqmme.mcserver.gui.selectorgui.openSelectorGUI
import de.dqmme.mcserver.database.Database
import de.dqmme.mcserver.util.deserializeMini
import kotlinx.coroutines.launch
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

fun GUIBuilder<ForInventoryThreeByNine>.startPage(
    servers: List<Server>,
    slot: InventorySlot,
    setName: String?,
    setMaterial: Material?,
    setServer: Server?,
    openSelectItem: Boolean
) {
    page(if (openSelectItem) 2 else 1) {
        transitionTo = PageChangeEffect.SLIDE_VERTICALLY
        transitionFrom = PageChangeEffect.SLIDE_VERTICALLY

        setNameButton(servers, Slots.RowTwoSlotThree, slot, setName, setMaterial, setServer)

        pageChanger(
            Slots.RowTwoSlotFive,
            itemStack(Material.GRASS_BLOCK) {
                meta {
                    name = "<green>Item setzen".deserializeMini()

                    setLore {
                        +"<green>Setze, welches Item im Navigator angezeigt werden soll".deserializeMini()
                        if (setMaterial != null) +"<yellow>Aktuell: <aqua>${setMaterial.name}".deserializeMini()
                    }
                }
            },
            if (openSelectItem) 1 else 2,
            null,
            null
        )

        pageChanger(
            Slots.RowTwoSlotSeven,
            itemStack(Material.PISTON) {
                meta {
                    name = "<green>Server setzen".deserializeMini()

                    setLore {
                        +"<green>Setze, zu welchem Server das Item navigieren seoll".deserializeMini()
                        if (setServer != null) +"<yellow>Aktuell: <aqua>${setServer.name}".deserializeMini()
                    }
                }
            },
            3,
            null,
            null
        )

        if (setName != null && setMaterial != null && setServer != null) {
            pageChanger(Slots.RowThreeSlotNine, itemStack(Material.LIME_DYE) {
                meta {
                    name = "<green>Item setzen".deserializeMini()

                    setLore {
                        +"<green>Setze das Item".deserializeMini()
                    }
                }
            }, 4, null) {
                scope.launch {
                    navigatorConfig.setNavigatorItem(
                        setServer.id, NavigatorItem(
                            slot.row, slot.slotInRow, setName, setMaterial
                        )
                    )

                    Database.saveServer(setServer.copy(isOnNavigator = true))

                    it.player.openSelectorGUI(true)
                }
            }
        }

        button(Slots.RowThreeSlotOne, GUIItems.back) {
            scope.launch {
                it.player.openSelectorGUI(true)
            }
        }
    }
}