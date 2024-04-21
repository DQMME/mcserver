package de.dqmme.mcserver.gui.admingui.page.manageserver

import com.mattmalec.pterodactyl4j.UtilizationState
import com.mattmalec.pterodactyl4j.client.entities.ClientServer
import com.mattmalec.pterodactyl4j.client.entities.GenericFile
import com.mattmalec.pterodactyl4j.client.entities.Utilization
import de.dqmme.mcserver.api.PterodactylAPI
import de.dqmme.mcserver.config.impl.pluginConfig
import de.dqmme.mcserver.config.impl.serverPluginsConfig
import de.dqmme.mcserver.dataclass.ManageSingleServerPage
import de.dqmme.mcserver.dataclass.Server
import de.dqmme.mcserver.dataclass.ServerPlugin
import de.dqmme.mcserver.gui.admingui.button.manageserver.serverInfoPlaceholder
import de.dqmme.mcserver.gui.admingui.button.manageserver.stateButtons
import de.dqmme.mcserver.gui.admingui.button.manageserver.updateInfoButton
import de.dqmme.mcserver.gui.admingui.gui.openReloadedManageSingleServerGUI
import de.dqmme.mcserver.gui.admingui.scope
import de.dqmme.mcserver.item.Skulls
import de.dqmme.mcserver.util.deserializeMini
import de.dqmme.mcserver.util.getPageNumbers
import kotlinx.coroutines.launch
import net.axay.kspigot.gui.ForInventoryFiveByNine
import net.axay.kspigot.gui.GUIBuilder
import net.axay.kspigot.gui.PageChangeEffect
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.setLore
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

fun GUIBuilder<ForInventoryFiveByNine>.downloadPredefinedPluginsPage(
    server: Server,
    serverInfo: ClientServer,
    utilization: Utilization?,
    plugins: List<GenericFile>,
    pageToOpen: ManageSingleServerPage = ManageSingleServerPage.START
) {
    val pageNumbers = getPageNumbers(pageToOpen)

    page(pageNumbers[ManageSingleServerPage.DOWNLOAD_PREDEFINED_PLUGINS]!!) {
        transitionTo = PageChangeEffect.SLIDE_VERTICALLY
        transitionFrom = PageChangeEffect.SLIDE_VERTICALLY

        val compound =
            createRectCompound<ServerPlugin>(
                Slots.RowOneSlotOne,
                Slots.RowFiveSlotEight,
                iconGenerator = { downloadPluginItem(it, utilization, plugins) },

                onClick = { clickEvent, element ->
                    scope.launch {
                        if (utilization == null || utilization.state != UtilizationState.RUNNING) return@launch

                        val newUtilization = PterodactylAPI.getUtilization(serverInfo)

                        if (newUtilization == null || newUtilization.state != UtilizationState.RUNNING) {
                            clickEvent.player.openReloadedManageSingleServerGUI(
                                server,
                                serverInfo,
                                pageToOpen = ManageSingleServerPage.DOWNLOAD_PREDEFINED_PLUGINS
                            )
                            return@launch
                        }

                        if (!server.installPlugin(element.link, element.name)) {
                            clickEvent.player.openReloadedManageSingleServerGUI(
                                server,
                                serverInfo,
                                pageToOpen = ManageSingleServerPage.DOWNLOAD_PREDEFINED_PLUGINS
                            )
                            return@launch
                        }

                        if (element.id.lowercase() == "butils") {
                            val bUtilsLicense = pluginConfig.getBUtilsLicense()
                            if (bUtilsLicense != null) server.sendCommand("setbutilslicense $bUtilsLicense")
                        }
                    }

                    clickEvent.bukkitEvent.isCancelled = true
                })

        compound.sortContentBy { it.name }

        compound.addContent(serverPluginsConfig.getServerPlugins())

        compoundScroll(
            Slots.RowFiveSlotNine, Skulls.arrowUp, compound, scrollTimes = 5, reverse = true
        )

        pageChanger(
            Slots.RowFourSlotNine,
            Skulls.arrowLeft,
            pageNumbers[ManageSingleServerPage.PLUGIN_MENU]!!,
            null,
            null
        )

        compoundScroll(
            Slots.RowThreeSlotNine, Skulls.arrowDown, compound, scrollTimes = 5
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
            ManageSingleServerPage.DOWNLOAD_PREDEFINED_PLUGINS
        )

        serverInfoPlaceholder(Slots.RowOneSlotNine, serverInfo, utilization)
    }
}

fun downloadPluginItem(serverPlugin: ServerPlugin, utilization: Utilization?, plugins: List<GenericFile>): ItemStack {
    if (utilization == null || utilization.state != UtilizationState.RUNNING) {
        return with(Skulls.grayArrowDown) {
            meta {
                name = "<gray>${serverPlugin.name}".deserializeMini()

                setLore {
                    +"<gray><bold>Plugins können nur heruntergeladen werden, wenn der Server läuft".deserializeMini()
                    +"<gray>Lade das Plugin ${serverPlugin.name} herunter".deserializeMini()
                }
            }
            this
        }
    }

    if (plugins.find { it.name == serverPlugin.name } != null) {
        return with(Skulls.yellowArrowDown) {
            meta {
                name = "<yellow>${serverPlugin.name}".deserializeMini()

                setLore {
                    +"<yellow><bold>Das Plugin ist bereits installiert. Beim Herunterladen wird es ersetzt".deserializeMini()
                    +"<yellow>Lade das Plugin ${serverPlugin.name} herunter".deserializeMini()
                }
            }
            this
        }
    }

    return with(Skulls.limeArrowDown) {
        meta {
            name = "<green>${serverPlugin.name}".deserializeMini()

            setLore {
                +"<red><bold>Klicke nur einmal! Aktualisiere dann die Seite mit der Uhr unten rechts.".deserializeMini()
                +"<green>Status: <red>Nicht installiert".deserializeMini()
                +"<green>Lade das Plugin ${serverPlugin.name} herunter".deserializeMini()
            }
        }
        this
    }
}