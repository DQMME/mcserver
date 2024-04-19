package de.dqmme.mcserver.event

import net.axay.kspigot.event.listen
import org.bukkit.event.entity.FoodLevelChangeEvent

fun registerFoodLevelChangeEvent() = listen<FoodLevelChangeEvent> {
    it.foodLevel = 20
    it.isCancelled = true
}