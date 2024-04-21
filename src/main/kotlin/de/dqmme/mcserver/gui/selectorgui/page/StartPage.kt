package de.dqmme.mcserver.gui.selectorgui.page

import com.mattmalec.pterodactyl4j.UtilizationState
import com.mattmalec.pterodactyl4j.client.entities.ClientServer
import com.mattmalec.pterodactyl4j.client.entities.Utilization
import de.dqmme.mcserver.api.PluginMessaging
import de.dqmme.mcserver.dataclass.NavigatorItem
import de.dqmme.mcserver.dataclass.Server
import de.dqmme.mcserver.util.Permissions
import de.dqmme.mcserver.util.coloredName
import de.dqmme.mcserver.util.deserializeMini
import net.axay.kspigot.gui.ForInventorySixByNine
import net.axay.kspigot.gui.GUIBuilder
import net.axay.kspigot.gui.PageChangeEffect
import net.axay.kspigot.gui.SingleInventorySlot
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.setLore
import org.bukkit.Material
import org.bukkit.entity.Player

fun GUIBuilder<ForInventorySixByNine>.startPage(
    player: Player,
    items: HashMap<Server, NavigatorItem>,
    serverInfos: HashMap<Long, ClientServer>,
    serverUtilizations: HashMap<Long, Utilization>,
    startManagePage: Boolean
) {
    page(if (startManagePage) 2 else 1) {
        transitionTo = PageChangeEffect.SLIDE_VERTICALLY
        transitionFrom = PageChangeEffect.SLIDE_VERTICALLY

        items.forEach {
            val server = it.key
            val item = it.value

            if (server.isPrivate && !server.invitedPlayers.contains(player.uniqueId.toString())) return@forEach

            val serverInfo = serverInfos[server.id] ?: return@forEach
            val utilization = serverUtilizations[server.id]

            button(
                SingleInventorySlot(item.row, item.slotInRow),
                itemStack(item.material) {
                    meta {
                        name = item.name.deserializeMini()

                        if (utilization != null && utilization.state == UtilizationState.RUNNING) {
                            setLore {
                                +"<aqua>Verbinde dich mit <yellow>${serverInfo.name}".deserializeMini()
                                +"<yellow>Status: ${utilization.state.coloredName()}".deserializeMini()
                            }
                        } else {
                            setLore {
                                +"<red>Der Server ist aktuell nicht verfügbar".deserializeMini()
                                val status = utilization?.state?.coloredName()
                                    ?: "<aqua><bold>Installiert gerade".deserializeMini()
                                +"<yellow>Status: $status".deserializeMini()
                            }
                        }
                    }
                }
            ) { event ->
                PluginMessaging.sendToServer(event.player, serverInfo.internalId)
            }
        }

        pageChanger(Slots.RowOneSlotNine, itemStack(Material.ENDER_PEARL) {
            meta {
                name = "<green>Private Server".deserializeMini()

                setLore {
                    +"<green>Verbinde dich mit privaten Servern, zu denen du eingeladen wurdest".deserializeMini()
                }
            }
        }, 5, null, null)

        if (player.hasPermission(Permissions.UPDATE_NAVIGATOR)) {
            pageChanger(Slots.RowOneSlotOne, itemStack(Material.STICK) {
                meta {
                    name = "<green>Navigator bearbeiten".deserializeMini()

                    setLore {
                        +"<green>Füge Server hinzu, entferne Server und ändere Slots".deserializeMini()
                    }
                }
            }, if (startManagePage) 1 else 2, null, null)
        }
    }
}