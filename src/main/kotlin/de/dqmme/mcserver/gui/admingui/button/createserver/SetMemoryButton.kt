package de.dqmme.mcserver.gui.admingui.button.createserver

import com.mattmalec.pterodactyl4j.DataType
import de.dqmme.mcserver.gui.admingui.gui.openCreateServerGUI
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

fun GUIPageBuilder<ForInventoryFiveByNine>.setMemoryButton(
    slot: InventorySlotCompound<ForInventoryFiveByNine>,
    setName: String? = null,
    setPort: Int? = null,
    setMemory: Pair<Long, DataType>? = null,
    setCPU: Long? = null,
    setDisk: Pair<Long, DataType>? = null
) {
    button(slot, itemStack(Material.REDSTONE) {
        meta {
            name = "<green>RAM setzen".deserializeMini()

            setLore {
                +"<green>Setze den RAM des Servers".deserializeMini()
                if (setMemory != null) +"<yellow>Aktuell: <aqua>${setMemory.first} ${setMemory.second.name}".deserializeMini()
            }
        }
    }) {
        SignGUI.builder()
            .setLine(0, "4 GB")
            .setLine(1, "Möglich: MB/GB")
            .setHandler { player, signGUIResult ->
                val memoryInput = signGUIResult.getLine(0)
                    .replace(" ", "")

                var dataType: DataType? = null

                if (memoryInput.contains("MB")) {
                    dataType = DataType.MB
                } else if (memoryInput.contains("GB")) {
                    dataType = DataType.GB
                }

                val memoryLong = memoryInput
                    .replace("MB", "")
                    .replace("GB", "")
                    .toLongOrNull()

                var memory: Pair<Long, DataType>? = null

                if (dataType != null && memoryLong != null) memory = memoryLong to dataType

                scope.launch {
                    player.openCreateServerGUI(setName, setPort, memory, setCPU, setDisk)
                }

                listOf(SignGUIAction.run { })
            }
            .build()
            .open(it.player)
    }
}