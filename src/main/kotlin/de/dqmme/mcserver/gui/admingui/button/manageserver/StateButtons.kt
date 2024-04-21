package de.dqmme.mcserver.gui.admingui.button.manageserver

import de.dqmme.mcserver.dataclass.Server
import de.dqmme.mcserver.gui.admingui.AdminGUIItems
import de.dqmme.mcserver.gui.admingui.scope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.axay.kspigot.gui.ForInventoryFiveByNine
import net.axay.kspigot.gui.GUIInstance
import net.axay.kspigot.gui.GUIPageBuilder
import net.axay.kspigot.gui.InventorySlotCompound
import kotlin.time.Duration.Companion.seconds

var stateCooldown = false

fun GUIPageBuilder<ForInventoryFiveByNine>.stateButtons(
    startButtonSlot: InventorySlotCompound<ForInventoryFiveByNine>,
    restartButtonSlot: InventorySlotCompound<ForInventoryFiveByNine>,
    stopButtonSlot: InventorySlotCompound<ForInventoryFiveByNine>,
    server: Server
) {
    fun GUIInstance<ForInventoryFiveByNine>.setCooldownItems() {
        set(startButtonSlot, AdminGUIItems.startServerItemCooldown)
        set(restartButtonSlot, AdminGUIItems.restartServerItemCooldown)
        set(stopButtonSlot, AdminGUIItems.stopServerItemCooldown)
    }

    fun GUIInstance<ForInventoryFiveByNine>.setItems() {
        set(startButtonSlot, AdminGUIItems.startServerItem)
        set(restartButtonSlot, AdminGUIItems.restartServerItem)
        set(stopButtonSlot, AdminGUIItems.stopServerItem)
    }

    button(
        slots = startButtonSlot,
        itemStack = if(stateCooldown) AdminGUIItems.startServerItemCooldown else AdminGUIItems.startServerItem
    ) onClick@{
        if(stateCooldown) return@onClick

        scope.launch {
            it.guiInstance.setCooldownItems()

            scope.launch {
                stateCooldown = true

                delay(5.seconds)

                stateCooldown = false

                it.guiInstance.setItems()
            }

            server.start()
            delay(0.5.seconds)
        }
    }

    button(
        slots = restartButtonSlot,
        itemStack = if(stateCooldown) AdminGUIItems.restartServerItemCooldown else AdminGUIItems.restartServerItem
    ) onClick@{
        if(stateCooldown) return@onClick

        scope.launch {
            it.guiInstance.setCooldownItems()

            scope.launch {
                stateCooldown = true

                delay(5.seconds)

                stateCooldown = false

                it.guiInstance.setItems()
            }

            server.restart()
            delay(0.5.seconds)
        }
    }

    button(
        slots = stopButtonSlot,
        itemStack = if(stateCooldown) AdminGUIItems.stopServerItemCooldown else AdminGUIItems.stopServerItem
    ) onClick@{
        if(stateCooldown) return@onClick

        scope.launch {
            it.guiInstance.setCooldownItems()

            scope.launch {
                stateCooldown = true

                delay(5.seconds)

                stateCooldown = false

                it.guiInstance.setItems()
            }

            server.stop()
            delay(0.5.seconds)
        }
    }
}