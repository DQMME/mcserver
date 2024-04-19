package de.dqmme.mcserver.item

import com.mattmalec.pterodactyl4j.UtilizationState
import com.mattmalec.pterodactyl4j.client.entities.ClientServer
import com.mattmalec.pterodactyl4j.client.entities.Utilization
import de.dqmme.mcserver.config.impl.getLanguage
import de.dqmme.mcserver.util.Colors
import de.dqmme.mcserver.util.coloredName
import de.dqmme.mcserver.util.cpuUsage
import de.dqmme.mcserver.util.deserializeMini
import de.dqmme.mcserver.util.memoryUsage
import de.dqmme.mcserver.util.storageUsage
import net.axay.kspigot.items.addLore
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.setLore
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

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

    val stateItems = hashMapOf(
        UtilizationState.OFFLINE to Material.RED_WOOL,
        UtilizationState.RUNNING to Material.LIME_WOOL,
        UtilizationState.STARTING to Material.YELLOW_WOOL,
        UtilizationState.STOPPING to Material.ORANGE_WOOL,
    )

    fun waitItem(message: String) = itemStack(Material.ORANGE_WOOL) {
        meta {
            name = "<${Colors.ORANGE}>Warte kurz".deserializeMini()

            setLore {
                +"<${Colors.ORANGE}>$message".deserializeMini()
            }
        }
    }

    fun serverInfoItem(serverInfo: ClientServer, utilization: Utilization?): ItemStack {
        if (utilization == null) {
            return itemStack(Material.CYAN_WOOL) {
                meta {
                    name = "<aqua>Server wird installiert".deserializeMini()

                    setLore {
                        +"<aqua>Der Server wird gerade installiert.".deserializeMini()
                        +"<green>Warte ein paar Minuten.".deserializeMini()
                    }
                }
            }
        }

        return itemStack(stateItems[utilization.state]!!) {
            meta {
                name = "<gold>${serverInfo.name}".deserializeMini()

                addLore {
                    +"<yellow>Status: ${utilization.state.coloredName()}".deserializeMini()

                    val memoryUsage = utilization.memoryUsage()
                    val memoryUsageString = "${memoryUsage.second} ${memoryUsage.first.typeName}"

                    val diskUsage = utilization.storageUsage()
                    val diskUsageString = "${diskUsage.second} ${diskUsage.first.typeName}"

                    +"<yellow>RAM: <aqua>$memoryUsageString / ${serverInfo.limits.memoryLong / 1024} GB".deserializeMini()

                    +"<yellow>CPU: <aqua>${utilization.cpuUsage()}% / ${serverInfo.limits.cpu}%".deserializeMini()

                    +"<yellow>Storage: <aqua>$diskUsageString / ${serverInfo.limits.diskLong / 1024} GB".deserializeMini()
                }
            }
        }
    }

    val serverNotInstalledPlaceholder = itemStack(Material.BARRIER) {
        meta {
            name = "<yellow>Server wird installiert".deserializeMini()

            setLore {
                +"<yellow>Dieses Feature ist erst nach der Installation verfügbar".deserializeMini()
                +"<yellow>Aktualisiere den Status mit der Uhr".deserializeMini()
            }
        }
    }

    val startServerItem = itemStack(Material.LIME_WOOL) {
        meta {
            name = "<green>Server starten".deserializeMini()

            setLore {
                +"<green>Starte den Server".deserializeMini()
            }
        }
    }

    val startServerItemCooldown = itemStack(Material.GRAY_WOOL) {
        meta {
            name = "<gray>Server starten".deserializeMini()

            setLore {
                +"<gray>Starte den Server".deserializeMini()
                +"<gray><bold>Du hast bereits eine Aktion ausgeführt. Warte 5 Sekunden.".deserializeMini()
            }
        }
    }

    val restartServerItem = itemStack(Material.YELLOW_WOOL) {
        meta {
            name = "<yellow>Server neu starten".deserializeMini()

            setLore {
                +"<yellow>Starte den Server neu".deserializeMini()
            }
        }
    }

    val restartServerItemCooldown = itemStack(Material.GRAY_WOOL) {
        meta {
            name = "<gray>Server neu starten".deserializeMini()

            setLore {
                +"<gray>Starte den Server neu".deserializeMini()
                +"<gray><bold>Du hast bereits eine Aktion ausgeführt. Warte 5 Sekunden.".deserializeMini()
            }
        }
    }

    val stopServerItem = itemStack(Material.RED_WOOL) {
        meta {
            name = "<red>Server stoppen".deserializeMini()

            setLore {
                +"<red>Stoppe den Server".deserializeMini()
            }
        }
    }

    val stopServerItemCooldown = itemStack(Material.GRAY_WOOL) {
        meta {
            name = "<gray>Server stoppen".deserializeMini()

            setLore {
                +"<gray>Stoppe den Server".deserializeMini()
                +"<gray><bold>Du hast bereits eine Aktion ausgeführt. Warte 5 Sekunden.".deserializeMini()
            }
        }
    }
}