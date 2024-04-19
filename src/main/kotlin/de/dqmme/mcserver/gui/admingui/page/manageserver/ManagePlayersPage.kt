package de.dqmme.mcserver.gui.admingui.page.manageserver

import com.mattmalec.pterodactyl4j.client.entities.ClientServer
import com.mattmalec.pterodactyl4j.client.entities.Utilization
import de.dqmme.mcserver.dataclass.ManageSingleServerPage
import de.dqmme.mcserver.dataclass.Server
import de.dqmme.mcserver.gui.admingui.button.manageserver.serverInfoPlaceholder
import de.dqmme.mcserver.gui.admingui.button.manageserver.updateInfoButton
import de.dqmme.mcserver.gui.admingui.gui.openReloadedManageSingleServerGUI
import de.dqmme.mcserver.gui.admingui.scope
import de.dqmme.mcserver.item.Skulls
import de.dqmme.mcserver.util.Database
import de.dqmme.mcserver.util.deserializeMini
import de.dqmme.mcserver.util.getPageNumbers
import de.rapha149.signgui.SignGUI
import de.rapha149.signgui.SignGUIAction
import kotlinx.coroutines.launch
import net.axay.kspigot.gui.ForInventoryFiveByNine
import net.axay.kspigot.gui.GUIBuilder
import net.axay.kspigot.gui.PageChangeEffect
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.setLore
import net.axay.kspigot.main.KSpigotMainInstance
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.inventory.meta.SkullMeta
import java.util.UUID

fun GUIBuilder<ForInventoryFiveByNine>.managePlayersPage(
    server: Server,
    serverInfo: ClientServer,
    utilization: Utilization?,
    pageToOpen: ManageSingleServerPage = ManageSingleServerPage.START
) {
    val pageNumbers = getPageNumbers(pageToOpen)

    page(pageNumbers[ManageSingleServerPage.MANAGE_PLAYERS]!!) {
        transitionTo = PageChangeEffect.SLIDE_VERTICALLY
        transitionFrom = PageChangeEffect.SLIDE_VERTICALLY

        val compound = createRectCompound<OfflinePlayer>(
            Slots.RowTwoSlotOne, Slots.RowFiveSlotEight,
            iconGenerator = { with(Skulls.getPlayerHead(it)) {
                meta<SkullMeta> {
                    setLore {
                        +"<red>Entferne diesen Spieler vom Server".deserializeMini()
                    }
                }
                this
            } },
            onClick = { clickEvent, element ->
                val newInvitedPlayers = server.invitedPlayers.toMutableList()

                newInvitedPlayers.remove(element.uniqueId.toString())

                scope.launch {
                    Database.saveServer(server.copy(invitedPlayers = newInvitedPlayers))
                    clickEvent.player.openReloadedManageSingleServerGUI(
                        server,
                        serverInfo,
                        true,
                        ManageSingleServerPage.MANAGE_PLAYERS
                    )
                }

                clickEvent.bukkitEvent.isCancelled = true
            })

        compound.sortContentBy { it.name }

        compound.addContent(
            server.invitedPlayers
                .stream()
                .map { KSpigotMainInstance.server.getOfflinePlayer(UUID.fromString(it)) }
                .toList()
        )

        compoundScroll(
            Slots.RowFiveSlotNine, Skulls.arrowUp, compound, scrollTimes = 5, reverse = true
        )

        pageChanger(
            Slots.RowFourSlotNine,
            Skulls.arrowLeft,
            pageNumbers[ManageSingleServerPage.START]!!,
            null,
            null
        )

        compoundScroll(
            Slots.RowThreeSlotNine, Skulls.arrowDown, compound, scrollTimes = 5
        )

        button(Slots.RowOneSlotOne, with(Skulls.plus) {
            meta {
                name = "<green>Spieler hinzufügen".deserializeMini()

                setLore {
                    +"<green>Füge einen Spieler hinzu, welcher dem Server beitreten kann".deserializeMini()
                }
            }
            this
        }) {
            SignGUI.builder()
                .setHandler { player, signGUIResult ->
                    scope.launch {
                        val offlinePlayer = KSpigotMainInstance.server.getOfflinePlayer(signGUIResult.getLine(0))
                        val newInvitedPlayers = server.invitedPlayers.toMutableList()

                        newInvitedPlayers.add(offlinePlayer.uniqueId.toString())

                        Database.saveServer(server.copy(invitedPlayers = newInvitedPlayers))
                        player.openReloadedManageSingleServerGUI(
                            server,
                            serverInfo,
                            true,
                            ManageSingleServerPage.MANAGE_PLAYERS
                        )
                    }

                    mutableListOf(SignGUIAction.run {  })
                }
                .build()
                .open(it.player)
        }

        updateInfoButton(
            Slots.RowTwoSlotNine,
            Material.CLOCK,
            server,
            serverInfo,
            ManageSingleServerPage.MANAGE_PLAYERS
        )

        serverInfoPlaceholder(Slots.RowOneSlotNine, serverInfo, utilization)
    }
}