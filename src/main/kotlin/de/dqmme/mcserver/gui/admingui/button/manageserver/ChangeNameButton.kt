package de.dqmme.mcserver.gui.admingui.button.manageserver

import com.mattmalec.pterodactyl4j.client.entities.ClientServer
import de.dqmme.mcserver.api.PterodactylAPI
import de.dqmme.mcserver.dataclass.Server
import de.dqmme.mcserver.gui.admingui.gui.openReloadedManageSingleServerGUI
import de.dqmme.mcserver.gui.admingui.scope
import de.dqmme.mcserver.database.Database
import de.dqmme.mcserver.util.closeInventorySync
import de.dqmme.mcserver.util.deserializeMini
import de.rapha149.signgui.SignGUI
import de.rapha149.signgui.SignGUIAction
import kotlinx.coroutines.launch
import net.axay.kspigot.gui.ForInventoryFiveByNine
import net.axay.kspigot.gui.GUIPageBuilder
import net.axay.kspigot.gui.InventorySlotCompound
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.setLore
import org.bukkit.Material

fun GUIPageBuilder<ForInventoryFiveByNine>.changeNameButton(
    slots: InventorySlotCompound<ForInventoryFiveByNine>,
    material: Material,
    server: Server,
    serverInfo: ClientServer
) {
    return button(slots, itemStack(material) {
        meta {
            name = "<green>Namen ändern".deserializeMini()

            setLore {
                +"<green>Ändere den Namen des Servers".deserializeMini()
                +"<green>Aktuell: <aqua>${serverInfo.name}".deserializeMini()
            }
        }
    }) {
        SignGUI.builder().setLine(0, serverInfo.name).setType(Material.OAK_SIGN)
            .setHandler { player, signGUIResult ->
                scope.launch {
                    val newName = signGUIResult.getLine(0)

                    if (!PterodactylAPI.changeServerName(serverInfo, newName)) {
                        player.closeInventorySync()
                        return@launch
                    }

                    Database.saveServer(server.copy(name = newName))

                    player.openReloadedManageSingleServerGUI(server, serverInfo)
                }

                mutableListOf(SignGUIAction.run {})
            }.build().open(it.player)
    }
}