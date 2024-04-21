package de.dqmme.mcserver.gui.admingui

import com.mattmalec.pterodactyl4j.UtilizationState
import com.mattmalec.pterodactyl4j.client.entities.ClientServer
import com.mattmalec.pterodactyl4j.client.entities.GenericFile
import com.mattmalec.pterodactyl4j.client.entities.Utilization
import de.dqmme.mcserver.dataclass.ServerPlugin
import de.dqmme.mcserver.gui.GUIItems
import de.dqmme.mcserver.item.Skulls
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

object AdminGUIItems {
    val createServer = with(Skulls.plus) {
        meta {
            name = "<gold>Server erstellen".deserializeMini()

            setLore {
                +"<gold>Erstelle einen Server".deserializeMini()
            }
        }
        this
    }

    val addPlayer = with(Skulls.plus) {
        meta {
            name = "<green>Spieler hinzufügen".deserializeMini()

            setLore {
                +"<green>Füge einen Spieler hinzu, welcher dem Server beitreten kann".deserializeMini()
            }
        }
        this
    }

    fun downloadPluginItem(serverPlugin: ServerPlugin, utilization: Utilization?, plugins: List<GenericFile>): ItemStack {
        if (utilization == null || utilization.state != UtilizationState.RUNNING) {
            return with(Skulls.grayArrowDown) {
                meta {
                    name = "<gray>${serverPlugin.name}".deserializeMini()

                    setLore {
                        +"<gray><bold>Plugins können nur heruntergeladen werden, wenn der Server läuft".deserializeMini()
                        +"<gray>Lade das Plugin ${serverPlugin.name} herunter".deserializeMini()
                    }
                }
                this
            }
        }

        if (plugins.find { it.name == serverPlugin.name } != null) {
            return with(Skulls.yellowArrowDown) {
                meta {
                    name = "<yellow>${serverPlugin.name}".deserializeMini()

                    setLore {
                        +"<yellow><bold>Das Plugin ist bereits installiert. Beim Herunterladen wird es ersetzt".deserializeMini()
                        +"<yellow>Lade das Plugin ${serverPlugin.name} herunter".deserializeMini()
                    }
                }
                this
            }
        }

        return with(Skulls.limeArrowDown) {
            meta {
                name = "<green>${serverPlugin.name}".deserializeMini()

                setLore {
                    +"<red><bold>Klicke nur einmal! Aktualisiere dann die Seite mit der Uhr unten rechts.".deserializeMini()
                    +"<green>Status: <red>Nicht installiert".deserializeMini()
                    +"<green>Lade das Plugin ${serverPlugin.name} herunter".deserializeMini()
                }
            }
            this
        }
    }

    fun deletePluginItem(genericFile: GenericFile, utilization: Utilization?): ItemStack {
        if (utilization == null || utilization.state != UtilizationState.OFFLINE) {
            return with(Skulls.grayX) {
                meta {
                    name = "<gray>${genericFile.name}".deserializeMini()

                    setLore {
                        +"<gray><bold>Du kannst Plugins nur löschen, wenn der Server offline ist".deserializeMini()
                        +"<gray>Wenn du den Server gestartet hast, klicke die Uhr um die Infos neu zu laden".deserializeMini()
                        +"<gray>Lösche das Plugin ${genericFile.name}".deserializeMini()
                    }
                }
                this
            }
        }

        return with(Skulls.redX) {
            meta {
                name = "<gold>${genericFile.name}".deserializeMini()

                setLore {
                    +"<red><bold>Klicke nur einmal! Aktualisiere dann mit der Uhr unten rechts".deserializeMini()
                    +"<red>Lösche das Plugin <yellow>${genericFile.name}".deserializeMini()
                    +"<red><bold>Der Server wird neu gestartet".deserializeMini()
                }
            }
            this
        }
    }

    val deleteServerItem = with(Skulls.redX) {
        meta {
            name = "<red><bold>Server löschen".deserializeMini()

            setLore {
                +"<red><bold>Lösche den Server".deserializeMini()
            }
        }
        this
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

        return itemStack(GUIItems.stateItems[utilization.state]!!) {
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