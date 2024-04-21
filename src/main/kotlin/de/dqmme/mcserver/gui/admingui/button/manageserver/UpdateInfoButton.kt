package de.dqmme.mcserver.gui.admingui.button.manageserver

import com.mattmalec.pterodactyl4j.client.entities.ClientServer
import de.dqmme.mcserver.dataclass.ManageSingleServerPage
import de.dqmme.mcserver.dataclass.Server
import de.dqmme.mcserver.gui.admingui.gui.openReloadedManageSingleServerGUI
import de.dqmme.mcserver.util.deserializeMini
import de.dqmme.mcserver.util.scope
import kotlinx.coroutines.launch
import net.axay.kspigot.gui.ForInventoryFiveByNine
import net.axay.kspigot.gui.GUIPageBuilder
import net.axay.kspigot.gui.InventorySlotCompound
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.setLore
import org.bukkit.Material

fun GUIPageBuilder<ForInventoryFiveByNine>.updateInfoButton(
    slots: InventorySlotCompound<ForInventoryFiveByNine>,
    material: Material,
    server: Server,
    serverInfo: ClientServer,
    defaultPage: ManageSingleServerPage = ManageSingleServerPage.START
) {
    button(slots, itemStack(material) {
        meta {
            name = "<yellow>Infos aktualisieren".deserializeMini()

            setLore {
                +"<yellow>Lade dieses GUI neu".deserializeMini()
                +"<gold>Damit werden auch alle Plugins neu abgerufen".deserializeMini()
            }
        }
    }) {
        scope.launch {
            it.player.openReloadedManageSingleServerGUI(server, serverInfo, true, defaultPage)
        }
    }
}