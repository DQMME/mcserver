package de.dqmme.mcserver.gui.admingui.page.manageserver

import com.mattmalec.pterodactyl4j.UtilizationState
import com.mattmalec.pterodactyl4j.client.entities.ClientServer
import com.mattmalec.pterodactyl4j.client.entities.GenericFile
import com.mattmalec.pterodactyl4j.client.entities.Utilization
import de.dqmme.mcserver.api.PterodactylAPI
import de.dqmme.mcserver.config.impl.pluginConfig
import de.dqmme.mcserver.dataclass.ManageSingleServerPage
import de.dqmme.mcserver.dataclass.Server
import de.dqmme.mcserver.gui.GUIItems
import de.dqmme.mcserver.gui.admingui.AdminGUIItems
import de.dqmme.mcserver.gui.admingui.button.manageserver.serverInfoPlaceholder
import de.dqmme.mcserver.gui.admingui.button.manageserver.stateButtons
import de.dqmme.mcserver.gui.admingui.button.manageserver.updateInfoButton
import de.dqmme.mcserver.gui.admingui.gui.openReloadedManageSingleServerGUI
import de.dqmme.mcserver.gui.admingui.scope
import de.dqmme.mcserver.util.getPageNumbers
import kotlinx.coroutines.launch
import net.axay.kspigot.gui.ForInventoryFiveByNine
import net.axay.kspigot.gui.GUIBuilder
import net.axay.kspigot.gui.PageChangeEffect
import net.axay.kspigot.gui.Slots
import org.bukkit.Material

fun GUIBuilder<ForInventoryFiveByNine>.deletePluginsPage(
    server: Server,
    serverInfo: ClientServer,
    utilization: Utilization?,
    plugins: List<GenericFile>,
    pageToOpen: ManageSingleServerPage = ManageSingleServerPage.START
) {
    val pageNumbers = getPageNumbers(pageToOpen)

    page(pageNumbers[ManageSingleServerPage.DELETE_PLUGINS]!!) {
        transitionTo = PageChangeEffect.SLIDE_VERTICALLY
        transitionFrom = PageChangeEffect.SLIDE_VERTICALLY

        val compound = createRectCompound<GenericFile>(
            Slots.RowTwoSlotOne, Slots.RowFiveSlotEight,
            iconGenerator = { AdminGUIItems.deletePluginItem(it, utilization) },
            onClick = { clickEvent, element ->
                scope.launch {
                    if (utilization == null || utilization.state != UtilizationState.OFFLINE) return@launch

                    val newUtilization = PterodactylAPI.getUtilization(serverInfo)

                    if (newUtilization == null || newUtilization.state != UtilizationState.OFFLINE) {
                        clickEvent.player.openReloadedManageSingleServerGUI(
                            server,
                            serverInfo,
                            pageToOpen = ManageSingleServerPage.DELETE_PLUGINS
                        )
                        return@launch
                    }

                    if (!PterodactylAPI.deleteFile(serverInfo, element)) {
                        clickEvent.player.openReloadedManageSingleServerGUI(
                            server,
                            serverInfo,
                            true,
                            ManageSingleServerPage.DELETE_PLUGINS
                        )
                        return@launch
                    }
                }

                clickEvent.bukkitEvent.isCancelled = true
            })

        compound.sortContentBy { it.name }

        compound.addContent(
            plugins.stream().filter { it.isFile && it.name != pluginConfig.getSubserverPluginName() }.toList()
        )

        compoundScroll(
            Slots.RowFiveSlotNine, GUIItems.scrollUp, compound, scrollTimes = 5, reverse = true
        )

        pageChanger(
            Slots.RowFourSlotNine,
            GUIItems.back,
            pageNumbers[ManageSingleServerPage.PLUGIN_MENU]!!,
            null,
            null
        )

        compoundScroll(
            Slots.RowThreeSlotNine, GUIItems.scrollDown, compound, scrollTimes = 5
        )

        stateButtons(
            Slots.RowOneSlotOne,
            Slots.RowOneSlotTwo,
            Slots.RowOneSlotThree,
            server
        )

        updateInfoButton(
            Slots.RowTwoSlotNine,
            Material.CLOCK,
            server,
            serverInfo,
            ManageSingleServerPage.DELETE_PLUGINS
        )

        serverInfoPlaceholder(Slots.RowOneSlotNine, serverInfo, utilization)
    }
}