package de.dqmme.mcserver.item

import de.dqmme.mcserver.config.impl.getLanguage
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import org.bukkit.Material

object Items {
    val selectorItem = itemStack(Material.NETHER_STAR) {
        meta {
            name = getLanguage("selector_name")
        }
    }

    val adminItem = itemStack(Material.REDSTONE_TORCH) {
        meta {
            name = getLanguage("admin_name")
        }
    }
}