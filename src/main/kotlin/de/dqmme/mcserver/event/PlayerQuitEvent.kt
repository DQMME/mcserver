package de.dqmme.mcserver.event

import de.dqmme.mcserver.gui.admingui.button.manageserver.inputLink
import de.dqmme.mcserver.gui.admingui.button.manageserver.runCommand
import de.dqmme.mcserver.rank.removeFromRankTeams
import net.axay.kspigot.event.listen
import org.bukkit.event.player.PlayerQuitEvent

fun registerPlayerQuitEvent() = listen<PlayerQuitEvent> {
    it.quitMessage(null)

    runCommand.remove(it.player.uniqueId)
    inputLink.remove(it.player.uniqueId)

    //Remove from rank teams
    it.player.removeFromRankTeams()
}