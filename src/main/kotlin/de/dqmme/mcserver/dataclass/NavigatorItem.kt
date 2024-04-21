package de.dqmme.mcserver.dataclass

import org.bukkit.Material

data class NavigatorItem(
    val row: Int,
    val slotInRow: Int,
    val name: String,
    val material: Material
)