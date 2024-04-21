package de.dqmme.mcserver.rank

import de.dqmme.mcserver.config.impl.rankConfig
import de.dqmme.mcserver.dataclass.Rank
import de.dqmme.mcserver.util.deserializeMini
import net.axay.kspigot.main.KSpigotMainInstance
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Team

object RankManager {
    private lateinit var ranks: List<Rank>
    private val teams = hashMapOf<String, Team>()

    fun createTeams() {
        val scoreboard = KSpigotMainInstance.server.scoreboardManager.mainScoreboard

        ranks = rankConfig.getRanks()

        ranks.forEach {
            var team = scoreboard.getTeam("${it.priority}${it.id}")

            if (team == null) {
                team = scoreboard.registerNewTeam("${it.priority}${it.id}")
            }

            if(it.prefix != null) team.prefix("${it.prefix} ".deserializeMini())
            if(it.suffix != null) team.suffix(" ${it.suffix}".deserializeMini())

            teams[it.id] = team
        }
    }

    fun getRank(player: Player): Rank? {
        var rank: Rank? = null

        ranks.sortedByDescending { it.priority }.forEach {
            if (it.permission != null && !player.hasPermission(it.permission)) return@forEach

            rank = it
        }

        return rank
    }

    fun addPlayer(player: Player) {
        val rank = getRank(player) ?: return
        val team = teams[rank.id] ?: return

        team.addPlayer(player)
    }

    fun removePlayer(player: Player) {
        teams.forEach {
            it.value.removePlayer(player)
        }
    }
}