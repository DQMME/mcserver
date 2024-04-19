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

fun GUIPageBuilder<ForInventoryFiveByNine>.setCPUButton(
    slot: InventorySlotCompound<ForInventoryFiveByNine>,
    setName: String? = null,
    setPort: Int? = null,
    setMemory: Pair<Long, DataType>? = null,
    setCPU: Long? = null,
    setDisk: Pair<Long, DataType>? = null
) {
    button(slot, itemStack(Material.BEACON) {
        meta {
            name = "<green>CPU setzen".deserializeMini()

            setLore {
                +"<green>Setze wie viel % CPU der Server nutzen kann".deserializeMini()
                if(setPort != null) +"<yellow>Aktuell: <aqua>$setCPU".deserializeMini()
            }
        }
    }) {
        SignGUI.builder()
            .setLine(0, "100%")
            .setHandler { player, signGUIResult ->
                val cpu = signGUIResult.getLine(0).replace("%", "").toLongOrNull()

                scope.launch {
                    player.openCreateServerGUI(setName, setPort, setMemory, cpu, setDisk)
                }

                listOf(SignGUIAction.run {  })
            }
            .build()
            .open(it.player)
    }
}