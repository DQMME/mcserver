package de.dqmme.mcserver.gui.admingui.button.manageserver

import com.mattmalec.pterodactyl4j.client.entities.ClientServer
import de.dqmme.mcserver.api.PterodactylAPI
import de.dqmme.mcserver.dataclass.Server
import de.dqmme.mcserver.gui.admingui.gui.openReloadedManageSingleServerGUI
import de.dqmme.mcserver.util.deserializeMini
import de.dqmme.mcserver.util.scope
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

fun GUIPageBuilder<ForInventoryFiveByNine>.changeSpecsButton(
    slots: InventorySlotCompound<ForInventoryFiveByNine>,
    material: Material,
    server: Server,
    serverInfo: ClientServer
) {
    return button(slots, itemStack(material) {
        meta {
            name = "<green>Specs ändern".deserializeMini()

            setLore {
                +"<green>Ändere <yellow>RAM, CPU <green>und <yellow>Storage".deserializeMini()
                +"<green>Aktuell:".deserializeMini()
                +"<yellow>RAM: <aqua>${serverInfo.limits.memoryLong / 1024} GB".deserializeMini()
                +"<yellow>CPU: <aqua>${serverInfo.limits.cpu} %".deserializeMini()
                +"<yellow>Storage: <aqua>${serverInfo.limits.diskLong / 1024} GB".deserializeMini()
            }
        }
    }) {
        SignGUI.builder()
            .setLine(0, "RAM: ${serverInfo.limits.memoryLong / 1024} GB")
            .setLine(1, "CPU: ${serverInfo.limits.cpu} %")
            .setLine(2, "Storage: ${serverInfo.limits.diskLong / 1024} GB")
            .setType(Material.OAK_SIGN)
            .setHandler { player, signGUIResult ->
                scope.launch {
                    val newMemory = signGUIResult.getLine(0)
                        .replace(" ", "")
                        .replace("RAM:", "")
                        .replace("GB", "")
                        .toLongOrNull()

                    val newCPU = signGUIResult.getLine(1)
                        .replace(" ", "")
                        .replace("CPU:", "")
                        .replace("%", "")
                        .toLongOrNull()

                    val newStorage = signGUIResult.getLine(2)
                        .replace(" ", "")
                        .replace("Storage:", "")
                        .replace("GB", "")
                        .toLongOrNull()

                    if (newMemory == null || newCPU == null || newStorage == null) {
                        player.openReloadedManageSingleServerGUI(server, serverInfo)
                        return@launch
                    }

                    if (!PterodactylAPI.editServerSpecs(serverInfo, newMemory, newCPU, newStorage)) {
                        player.openReloadedManageSingleServerGUI(server, serverInfo)
                        return@launch
                    }

                    player.openReloadedManageSingleServerGUI(server, serverInfo)
                }

                mutableListOf(SignGUIAction.run {})
            }.build().open(it.player)
    }
}