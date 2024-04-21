package de.dqmme.mcserver.gui.admingui.button.createserver

import com.mattmalec.pterodactyl4j.DataType
import de.dqmme.mcserver.gui.admingui.gui.openCreateServerGUI
import de.dqmme.mcserver.gui.admingui.scope
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

fun GUIPageBuilder<ForInventoryFiveByNine>.setNameButton(
    slot: InventorySlotCompound<ForInventoryFiveByNine>,
    setName: String? = null,
    setPort: Int? = null,
    setMemory: Pair<Long, DataType>? = null,
    setCPU: Long? = null,
    setDisk: Pair<Long, DataType>? = null
) {
    button(slot, itemStack(Material.NAME_TAG) {
        meta {
            name = "<green>Namen setzen".deserializeMini()

            setLore {
                +"<green>Setze den Namen des Servers".deserializeMini()
                if (setName != null) +"<yellow>Aktuell: <aqua>$setName".deserializeMini()
            }
        }
    }) {
        SignGUI.builder()
            .setHandler { player, signGUIResult ->
                var name = signGUIResult.getLine(0)

                if (name.isEmpty()) name = null

                scope.launch {
                    player.openCreateServerGUI(name, setPort, setMemory, setCPU, setDisk)
                }

                listOf(SignGUIAction.run { })
            }
            .build()
            .open(it.player)
    }
}