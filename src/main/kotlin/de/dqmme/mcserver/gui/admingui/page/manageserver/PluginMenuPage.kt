package de.dqmme.mcserver.gui.admingui.page.manageserver

import com.mattmalec.pterodactyl4j.client.entities.ClientServer
import com.mattmalec.pterodactyl4j.client.entities.Utilization
import de.dqmme.mcserver.dataclass.ManageSingleServerPage
import de.dqmme.mcserver.dataclass.Server
import de.dqmme.mcserver.gui.GUIItems
import de.dqmme.mcserver.gui.admingui.button.manageserver.addOwnPluginsButton
import de.dqmme.mcserver.gui.admingui.button.manageserver.serverInfoPlaceholder
import de.dqmme.mcserver.gui.admingui.button.manageserver.updateInfoButton
import de.dqmme.mcserver.util.deserializeMini
import de.dqmme.mcserver.util.getPageNumbers
import net.axay.kspigot.gui.ForInventoryFiveByNine
import net.axay.kspigot.gui.GUIBuilder
import net.axay.kspigot.gui.PageChangeEffect
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.setLore
import org.bukkit.Material

fun GUIBuilder<ForInventoryFiveByNine>.pluginMenuPage(
    server: Server,
    serverInfo: ClientServer,
    utilization: Utilization?,
    pageToOpen: ManageSingleServerPage = ManageSingleServerPage.START
) {
    val pageNumbers = getPageNumbers(pageToOpen)

    page(pageNumbers[ManageSingleServerPage.PLUGIN_MENU]!!) {
        transitionTo = PageChangeEffect.SLIDE_VERTICALLY
        transitionFrom = PageChangeEffect.SLIDE_VERTICALLY

        pageChanger(
            Slots.RowFiveSlotOne,
            GUIItems.back,
            pageNumbers[ManageSingleServerPage.START]!!,
            null,
            null
        )

        pageChanger(Slots.RowThreeSlotThree, itemStack(Material.ENDER_EYE) {
            meta {
                name = "<green>Vorgefertigte Plugins hinzufügen".deserializeMini()

                setLore {
                    +"<green>Füge vorgefertigte Plugins hinzu".deserializeMini()
                }
            }
        }, pageNumbers[ManageSingleServerPage.DOWNLOAD_PREDEFINED_PLUGINS]!!, null, null)

        addOwnPluginsButton(Slots.RowThreeSlotFive, Material.ENDER_PEARL, server)

        pageChanger(Slots.RowThreeSlotSeven, itemStack(Material.REDSTONE_BLOCK) {
            meta {
                name = "<red>Plugins löschen".deserializeMini()

                setLore {
                    +"<red>Lösche Plugins vom Server".deserializeMini()
                    +"<red><bold>Der Server wird neu gestartet".deserializeMini()
                }
            }
        }, pageNumbers[ManageSingleServerPage.DELETE_PLUGINS]!!, null, null)

        updateInfoButton(
            Slots.RowTwoSlotNine,
            Material.CLOCK,
            server,
            serverInfo,
            ManageSingleServerPage.PLUGIN_MENU
        )

        serverInfoPlaceholder(Slots.RowOneSlotNine, serverInfo, utilization)
    }
}