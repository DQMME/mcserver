package de.dqmme.mcserver.event

import net.axay.kspigot.event.listen
import org.bukkit.entity.EntityType
import org.bukkit.event.entity.EntityDamageEvent

fun registerDamageEvent() = listen<EntityDamageEvent> {
    if (it.entityType != EntityType.PLAYER) return@listen

    it.isCancelled = true
}