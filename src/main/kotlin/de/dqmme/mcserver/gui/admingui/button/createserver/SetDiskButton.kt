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

fun GUIPageBuilder<ForInventoryFiveByNine>.setDiskButton(
    slot: InventorySlotCompound<ForInventoryFiveByNine>,
    setName: String? = null,
    setPort: Int? = null,
    setMemory: Pair<Long, DataType>? = null,
    setCPU: Long? = null,
    setDisk: Pair<Long, DataType>? = null
) {
    button(slot, itemStack(Material.CHEST) {
        meta {
            name = "<green>Speicherplatz setzen".deserializeMini()

            setLore {
                +"<green>Setze den Speicherplatz des Servers".deserializeMini()
                if (setDisk != null) +"<yellow>Aktuell: <aqua>${setDisk.first} ${setDisk.second.name}".deserializeMini()
            }
        }
    }) {
        SignGUI.builder()
            .setLine(0, "5 GB")
            .setLine(1, "Möglich: MB/GB")
            .setHandler { player, signGUIResult ->
                val diskInput = signGUIResult.getLine(0)
                    .replace(" ", "")

                var dataType: DataType? = null

                if (diskInput.contains("MB")) {
                    dataType = DataType.MB
                } else if (diskInput.contains("GB")) {
                    dataType = DataType.GB
                }

                val diskLong = diskInput
                    .replace("MB", "")
                    .replace("GB", "")
                    .toLongOrNull()

                var disk: Pair<Long, DataType>? = null

                if(dataType != null && diskLong != null) disk = diskLong to dataType

                scope.launch {
                    player.openCreateServerGUI(setName, setPort, setMemory, setCPU, disk)
                }

                listOf(SignGUIAction.run { })
            }
            .build()
            .open(it.player)
    }
}