package de.dqmme.mcserver.gui.admingui.page

import de.dqmme.mcserver.gui.admingui.button.createserver.setCPUButton
import de.dqmme.mcserver.gui.admingui.button.createserver.setDiskButton
import de.dqmme.mcserver.gui.admingui.button.createserver.setMemoryButton
import de.dqmme.mcserver.gui.admingui.button.createserver.setNameButton
import de.dqmme.mcserver.gui.admingui.button.createserver.setPortButton
import de.dqmme.mcserver.gui.admingui.button.createserver.setPrivateButton
import de.dqmme.mcserver.item.Skulls
import net.axay.kspigot.gui.ForInventoryFiveByNine
import net.axay.kspigot.gui.GUIBuilder
import net.axay.kspigot.gui.PageChangeEffect
import net.axay.kspigot.gui.Slots

fun GUIBuilder<ForInventoryFiveByNine>.createServerPage() {
    return page(3) {
        transitionTo = PageChangeEffect.SLIDE_VERTICALLY
        transitionFrom = PageChangeEffect.SLIDE_VERTICALLY

        pageChanger(Slots.RowFiveSlotOne, Skulls.arrowLeft, 1, null, null)

        setNameButton(Slots.RowFourSlotFive)

        setPortButton(Slots.RowTwoSlotFive)

        setMemoryButton(Slots.RowThreeSlotFour)

        setCPUButton(Slots.RowThreeSlotFive)

        setDiskButton(Slots.RowThreeSlotSix)

        setPrivateButton(Slots.RowFiveSlotNine)
    }
}