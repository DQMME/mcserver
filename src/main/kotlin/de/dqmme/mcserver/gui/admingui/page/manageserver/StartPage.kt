package de.dqmme.mcserver.gui.admingui.page.manageserver

import com.mattmalec.pterodactyl4j.client.entities.ClientServer
import com.mattmalec.pterodactyl4j.client.entities.Utilization
import de.dqmme.mcserver.dataclass.ManageSingleServerPage
import de.dqmme.mcserver.dataclass.Server
import de.dqmme.mcserver.gui.GUIItems
import de.dqmme.mcserver.gui.admingui.AdminGUIItems
import de.dqmme.mcserver.gui.admingui.button.manageserver.changeNameButton
import de.dqmme.mcserver.gui.admingui.button.manageserver.changeSpecsButton
import de.dqmme.mcserver.gui.admingui.button.manageserver.deleteServerButton
import de.dqmme.mcserver.gui.admingui.button.manageserver.runCommandButton
import de.dqmme.mcserver.gui.admingui.button.manageserver.serverInfoPlaceholder
import de.dqmme.mcserver.gui.admingui.button.manageserver.setPrivateButton
import de.dqmme.mcserver.gui.admingui.button.manageserver.stateButtons
import de.dqmme.mcserver.gui.admingui.button.manageserver.updateInfoButton
import de.dqmme.mcserver.gui.admingui.openAdminGUI
import de.dqmme.mcserver.gui.admingui.scope
import de.dqmme.mcserver.util.deserializeMini
import de.dqmme.mcserver.util.getPageNumbers
import kotlinx.coroutines.launch
import net.axay.kspigot.gui.ForInventoryFiveByNine
import net.axay.kspigot.gui.GUIBuilder
import net.axay.kspigot.gui.PageChangeEffect
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.setLore
import org.bukkit.Material

fun GUIBuilder<ForInventoryFiveByNine>.startPage(
    server: Server,
    serverInfo: ClientServer,
    utilization: Utilization?,
    pageToOpen: ManageSingleServerPage = ManageSingleServerPage.START
) {
    val pageNumbers = getPageNumbers(pageToOpen)

    page(pageNumbers[ManageSingleServerPage.START]!!) {
        transitionTo = PageChangeEffect.SLIDE_VERTICALLY
        transitionFrom = PageChangeEffect.SLIDE_VERTICALLY

        button(Slots.RowFiveSlotOne, GUIItems.back) {
            scope.launch {
                it.player.openAdminGUI(true)
            }
        }

        updateInfoButton(
            Slots.RowTwoSlotNine,
            Material.CLOCK,
            server,
            serverInfo,
            ManageSingleServerPage.START
        )

        serverInfoPlaceholder(Slots.RowOneSlotNine, serverInfo, utilization)

        changeNameButton(Slots.RowFourSlotFive, Material.NAME_TAG, server, serverInfo)

        changeSpecsButton(Slots.RowThreeSlotFour, Material.SOUL_TORCH, server, serverInfo)

        setPrivateButton(Slots.RowFourSlotNine, server)

        if(utilization == null) {
            placeholder(Slots.RowThreeSlotSix, AdminGUIItems.serverNotInstalledPlaceholder)
            placeholder(Slots.RowTwoSlotFive, AdminGUIItems.serverNotInstalledPlaceholder)
            placeholder(Slots.RowFiveSlotNine, AdminGUIItems.serverNotInstalledPlaceholder)
            placeholder(Slots.RowThreeSlotFive, AdminGUIItems.serverNotInstalledPlaceholder)
            placeholder(Slots.RowThreeSlotOne, AdminGUIItems.serverNotInstalledPlaceholder)
            placeholder(Slots.RowTwoSlotOne, AdminGUIItems.serverNotInstalledPlaceholder)
            placeholder(Slots.RowOneSlotOne, AdminGUIItems.serverNotInstalledPlaceholder)
            return@page
        }

        runCommandButton(Slots.RowThreeSlotSix, Material.COMMAND_BLOCK, serverInfo)

        pageChanger(Slots.RowTwoSlotFive, itemStack(Material.BOOKSHELF) {
            meta {
                name = "<green>Plugins verwalten".deserializeMini()

                setLore {
                    +"<green>Verwalte die Plugins des Servers".deserializeMini()
                }
            }
        }, pageNumbers[ManageSingleServerPage.PLUGIN_MENU]!!, null, null)

        pageChanger(Slots.RowThreeSlotFive, itemStack(Material.CAMPFIRE) {
            meta {
                name = "<green>Spieler verwalten".deserializeMini()

                setLore {
                    +"<green>Verwalte welche Spieler dem Server beitreten können".deserializeMini()
                }
            }
        }, pageNumbers[ManageSingleServerPage.MANAGE_PLAYERS]!!, null, null)

        deleteServerButton(Slots.RowFiveSlotNine, serverInfo)

        stateButtons(
            Slots.RowThreeSlotOne,
            Slots.RowTwoSlotOne,
            Slots.RowOneSlotOne,
            server
        )
    }
}