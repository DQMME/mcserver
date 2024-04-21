package de.dqmme.mcserver.util

import com.mattmalec.pterodactyl4j.UtilizationState
import com.mattmalec.pterodactyl4j.client.entities.Utilization
import de.dqmme.mcserver.dataclass.ManageSingleServerPage
import de.dqmme.mcserver.dataclass.StorageType
import net.axay.kspigot.main.KSpigotMainInstance
import net.axay.kspigot.runnables.task
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Difficulty
import org.bukkit.GameRule
import org.bukkit.entity.Player

fun String.deserializeMini() = MiniMessage.miniMessage().deserialize(this)

fun Utilization.memoryUsage(): Pair<StorageType, Double> {
    val gigabytes = memory / (1024.0 * 1024.0 * 1024.0)

    if (gigabytes < 1) {
        val mb = memory / (1024.0 * 1024.0)
        return StorageType.MB to String.format("%.1f", mb).toDouble()
    }

    return StorageType.GB to String.format("%.1f", gigabytes).toDouble()
}

fun Utilization.cpuUsage(): Double {
    val gigabytes = cpu
    return String.format("%.1f", gigabytes).toDouble()
}

fun Utilization.storageUsage(): Pair<StorageType, Double> {
    val gigabytes = disk / (1024.0 * 1024.0 * 1024.0)

    if (gigabytes < 1) {
        val mb = disk / (1024.0 * 1024.0)
        return StorageType.MB to String.format("%.1f", mb).toDouble()
    }

    return StorageType.GB to String.format("%.1f", gigabytes).toDouble()
}

fun getPageNumbers(
    pageToStart: ManageSingleServerPage
): HashMap<ManageSingleServerPage, Int> {
    if (pageToStart == ManageSingleServerPage.START) {
        return ManageSingleServerPage.default
    }

    val numbers = hashMapOf<ManageSingleServerPage, Int>()

    ManageSingleServerPage.entries.forEach {
        if (it != pageToStart && it != ManageSingleServerPage.START) {
            numbers[it] = it.defaultNumber
            return@forEach
        }
    }

    numbers[ManageSingleServerPage.START] = pageToStart.defaultNumber
    numbers[pageToStart] = 1

    return numbers
}

fun UtilizationState.coloredName(): String {
    return when (this) {
        UtilizationState.OFFLINE -> "<red><bold>Offline"
        UtilizationState.STARTING -> "<yellow><bold>Startet"
        UtilizationState.RUNNING -> "<green><bold>Online"
        UtilizationState.STOPPING -> "<yellow><bold>Wird gestoppt"
    }
}

fun prepareWorlds() {
    KSpigotMainInstance.server.worlds.forEach {
        it.difficulty = Difficulty.PEACEFUL
        it.time = 0
        it.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false)
    }
}

fun Player.closeInventorySync() = task(true) {
    closeInventory()
}