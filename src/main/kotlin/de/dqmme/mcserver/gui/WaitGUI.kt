package de.dqmme.mcserver.gui

import de.dqmme.mcserver.item.Items
import net.axay.kspigot.gui.GUIType
import net.axay.kspigot.gui.PageChangeEffect
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.gui.kSpigotGUI
import net.axay.kspigot.gui.openGUI
import net.axay.kspigot.runnables.task
import org.bukkit.entity.Player

fun Player.openWaitGUI() = task(true) {
    openGUI(kSpigotGUI(GUIType.THREE_BY_NINE) {
        page(1) {
            transitionTo = PageChangeEffect.SLIDE_VERTICALLY
            transitionFrom = PageChangeEffect.SLIDE_VERTICALLY

            placeholder(Slots.RowTwoSlotFive, Items.waitItem("Alle Infos werden geladen, lehn dich zurück."))
        }
    })
}