package de.dqmme.mcserver.gui.admingui.page

import com.mattmalec.pterodactyl4j.client.entities.ClientServer
import com.mattmalec.pterodactyl4j.client.entities.GenericFile
import com.mattmalec.pterodactyl4j.client.entities.Utilization
import de.dqmme.mcserver.dataclass.Server
import de.dqmme.mcserver.gui.GUIItems
import de.dqmme.mcserver.gui.admingui.AdminGUIItems
import de.dqmme.mcserver.gui.admingui.gui.openManageSingleServerGUI
import de.dqmme.mcserver.gui.admingui.openAdminGUI
import de.dqmme.mcserver.gui.admingui.scope
import de.dqmme.mcserver.util.deserializeMini
import kotlinx.coroutines.launch
import net.axay.kspigot.gui.ForInventoryFiveByNine
import net.axay.kspigot.gui.GUIBuilder
import net.axay.kspigot.gui.PageChangeEffect
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.items.addLore
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.setLore
import org.bukkit.Material

fun GUIBuilder<ForInventoryFiveByNine>.manageServersPage(
    startPage: Boolean,
    servers: MutableList<Server>,
    serverInfos: HashMap<Long, ClientServer>,
    serverUtilization: HashMap<Long, Utilization>,
    serverPlugins: HashMap<Long, List<GenericFile>>
) {
    page(if (startPage) 1 else 2) {
        transitionTo = PageChangeEffect.SLIDE_VERTICALLY
        transitionFrom = PageChangeEffect.SLIDE_VERTICALLY

        val compound = createRectCompound<Server>(
            Slots.RowOneSlotOne, Slots.RowFiveSlotEight, iconGenerator = {
                val serverInfo = serverInfos[it.id]!!
                val utilization = serverUtilization[it.id]

                with(AdminGUIItems.serverInfoItem(serverInfo, utilization)) {
                    meta {
                        addLore {
                            +"<gold>Klicke um die Server-Infos zu bearbeiten.".deserializeMini()
                        }
                    }
                    this
                }
            },

            onClick = onClick@{ clickEvent, element ->
                val serverInfo = serverInfos[element.id]
                val utilization = serverUtilization[element.id]
                val plugins = serverPlugins[element.id] ?: emptyList()

                if (serverInfo == null) {
                    clickEvent.bukkitEvent.isCancelled = true
                    return@onClick
                }

                clickEvent.player.closeInventory()
                clickEvent.player.openManageSingleServerGUI(element, serverInfo, utilization, plugins)
                return@onClick
            })

        compound.sortContentBy { serverInfos[it.id]?.name ?: "" }

        compound.addContent(servers)

        compoundScroll(
            Slots.RowFiveSlotNine, GUIItems.scrollUp, compound, scrollTimes = 5, reverse = true
        )

        pageChanger(Slots.RowFourSlotNine, GUIItems.back, if (!startPage) 1 else 2, null, null)

        compoundScroll(
            Slots.RowThreeSlotNine, GUIItems.scrollDown, compound, scrollTimes = 5
        )

        button(Slots.RowOneSlotNine, itemStack(Material.CLOCK) {
            meta {
                name = "<yellow>Server aktualisieren".deserializeMini()

                setLore {
                    +"<yellow>Lade dieses GUI neu".deserializeMini()
                }
            }
        }) {
            scope.launch {
                it.player.openAdminGUI(true)
            }
        }
    }
}