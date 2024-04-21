package de.dqmme.mcserver.gui.admingui.button.manageserver

import com.mattmalec.pterodactyl4j.client.entities.ClientServer
import de.dqmme.mcserver.api.PterodactylAPI
import de.dqmme.mcserver.config.impl.navigatorConfig
import de.dqmme.mcserver.gui.admingui.openAdminGUI
import de.dqmme.mcserver.gui.admingui.scope
import de.dqmme.mcserver.item.Skulls
import de.dqmme.mcserver.util.Database
import de.dqmme.mcserver.util.deserializeMini
import kotlinx.coroutines.launch
import net.axay.kspigot.gui.ForInventoryFiveByNine
import net.axay.kspigot.gui.GUIPageBuilder
import net.axay.kspigot.gui.GUIType
import net.axay.kspigot.gui.InventorySlotCompound
import net.axay.kspigot.gui.PageChangeEffect
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.gui.kSpigotGUI
import net.axay.kspigot.gui.openGUI
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.setLore
import org.bukkit.Material

fun GUIPageBuilder<ForInventoryFiveByNine>.deleteServerButton(
    slots: InventorySlotCompound<ForInventoryFiveByNine>,
    serverInfo: ClientServer
) {
    val confirmGUI = kSpigotGUI(GUIType.FIVE_BY_NINE) {
        title = "<red>Server wirklich löschen".deserializeMini()

        page(1) {
            transitionTo = PageChangeEffect.SLIDE_VERTICALLY
            transitionFrom = PageChangeEffect.SLIDE_VERTICALLY

            button(Slots.RowThreeSlotFour, itemStack(Material.LIME_WOOL) {
                meta {
                    name = "<green>Abbrechen".deserializeMini()

                    setLore {
                        +"<green>Lösche den Server nicht und kehre zurück".deserializeMini()
                    }
                }
            }) {
                scope.launch {
                    it.player.openAdminGUI(true)
                }
            }

            pageChanger(Slots.RowThreeSlotSix, itemStack(Material.RED_WOOL) {
                meta {
                    name = "<red><bold>Server löschen".deserializeMini()

                    setLore {
                        +"<red><bold>Lösche den Server permanent (unwiderruflich)".deserializeMini()
                    }
                }
            }, 2, null) onChange@{
                scope.launch {
                    Database.deleteServer(serverInfo.internalIdLong)

                    PterodactylAPI.deleteServer(serverInfo)
                    PterodactylAPI.removeProxyServer(serverInfo.internalIdLong)
                    navigatorConfig.deleteNavigatorItem(serverInfo.internalIdLong)

                    it.player.openAdminGUI(true)
                }
            }
        }

        page(2) {
            transitionTo = PageChangeEffect.SLIDE_VERTICALLY
            transitionFrom = PageChangeEffect.SLIDE_VERTICALLY

            placeholder(Slots.All, itemStack(Material.GRAY_STAINED_GLASS_PANE) {
                meta {
                    name = "<gray>Warte...".deserializeMini()

                    setLore {
                        "<gray>Der Server wird gelöscht.".deserializeMini()
                    }
                }
            })
        }
    }

    return button(slots, with(Skulls.redX) {
        meta {
            name = "<red><bold>Server löschen".deserializeMini()

            setLore {
                +"<red><bold>Lösche den Server".deserializeMini()
            }
        }
        this
    }) {
        it.player.openGUI(confirmGUI)
    }
}