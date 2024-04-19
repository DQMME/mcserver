package de.dqmme.mcserver.command

import com.mojang.brigadier.arguments.StringArgumentType
import de.dqmme.mcserver.config.impl.getLanguage
import de.dqmme.mcserver.config.impl.pluginConfig
import net.axay.kspigot.commands.argument
import net.axay.kspigot.commands.command
import net.axay.kspigot.commands.runs
import net.axay.kspigot.commands.suggestList

fun registerSetValueCommand() = command("setvalue", true) {
    val possibleArguments = listOf("spawn")
    val usage = getLanguage(
        "invalid_usage", hashMapOf(
            "[USAGE]" to "/setvalue <${possibleArguments.joinToString(", ")}>"
        )
    )

    argument("argument", StringArgumentType.string()) {
        runs {
            val argument = getArgument<String>("argument")

            if (!possibleArguments.contains(argument)) {
                player.sendMessage(usage)
                return@runs
            }

            when (argument) {
                "spawn" -> {
                    pluginConfig.setSpawnLocation(player.location)
                    player.sendMessage(
                        getLanguage(
                            "spawn_set", hashMapOf(
                                "[X]" to "${player.location.blockX}",
                                "[Y]" to "${player.location.blockY}",
                                "[Z]" to "${player.location.blockZ}"
                            )
                        )
                    )
                }
            }
        }

        suggestList {
            possibleArguments
        }
    }

    runs {
        player.sendMessage(usage)
    }
}