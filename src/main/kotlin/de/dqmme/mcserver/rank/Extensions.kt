package de.dqmme.mcserver.rank

import org.bukkit.entity.Player

fun Player.getRank() = RankManager.getRank(this)

fun Player.addToRankTeam() = RankManager.addPlayer(this)

fun Player.removeFromRankTeams() = RankManager.removePlayer(this)