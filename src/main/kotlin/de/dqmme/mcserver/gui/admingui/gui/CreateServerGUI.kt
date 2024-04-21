package de.dqmme.mcserver.gui.admingui.gui

import com.mattmalec.pterodactyl4j.DataType
import de.dqmme.mcserver.api.PterodactylAPI
import de.dqmme.mcserver.config.impl.pluginConfig
import de.dqmme.mcserver.gui.GUIItems
import de.dqmme.mcserver.gui.admingui.button.createserver.setCPUButton
import de.dqmme.mcserver.gui.admingui.button.createserver.setDiskButton
import de.dqmme.mcserver.gui.admingui.button.createserver.setMemoryButton
import de.dqmme.mcserver.gui.admingui.button.createserver.setNameButton
import de.dqmme.mcserver.gui.admingui.button.createserver.setPortButton
import de.dqmme.mcserver.gui.admingui.button.createserver.setPrivateButton
import de.dqmme.mcserver.gui.admingui.openAdminGUI
import de.dqmme.mcserver.util.deserializeMini
import de.dqmme.mcserver.util.scope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.axay.kspigot.gui.GUIType
import net.axay.kspigot.gui.PageChangeEffect
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.gui.kSpigotGUI
import net.axay.kspigot.gui.openGUI
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.setLore
import net.axay.kspigot.runnables.task
import org.bukkit.Material
import org.bukkit.entity.Player
import kotlin.time.Duration.Companion.seconds

var createPrivateServer = false

suspend fun Player.openCreateServerGUI(
    setName: String? = null,
    setPort: Int? = null,
    setMemory: Pair<Long, DataType>? = null,
    setCPU: Long? = null,
    setDisk: Pair<Long, DataType>? = null
) {
    var port = setPort

    if (port != null) {
        val available = PterodactylAPI.checkPortAvailable(port)

        if (!available) port = null
    }

    task(true) {
        openGUI(kSpigotGUI(GUIType.FIVE_BY_NINE) {
            page(1) {
                transitionTo = PageChangeEffect.SLIDE_VERTICALLY
                transitionFrom = PageChangeEffect.SLIDE_VERTICALLY

                button(Slots.RowFiveSlotOne, GUIItems.back) {
                    scope.launch {
                        it.player.openAdminGUI()
                    }
                }

                val createServer = setName != null &&
                        port != null &&
                        setMemory != null &&
                        setCPU != null &&
                        setDisk != null

                setNameButton(Slots.RowFourSlotFive, setName, port, setMemory, setCPU, setDisk)

                setPortButton(Slots.RowTwoSlotFive, setName, port, setMemory, setCPU, setDisk)

                setMemoryButton(Slots.RowThreeSlotFour, setName, port, setMemory, setCPU, setDisk)

                setCPUButton(Slots.RowThreeSlotFive, setName, port, setMemory, setCPU, setDisk)

                setDiskButton(Slots.RowThreeSlotSix, setName, port, setMemory, setCPU, setDisk)

                setPrivateButton(if (createServer) Slots.RowFourSlotNine else Slots.RowFiveSlotNine)

                if (setPort != null && port != null) {
                    placeholder(Slots.RowOneSlotFive, itemStack(Material.LIME_WOOL) {
                        meta {
                            name = "<green>Port verfügbar".deserializeMini()

                            setLore {
                                +"<green>Der Port ist verfügbar".deserializeMini()
                            }
                        }
                    })
                }

                if (setPort != null && port == null) {
                    placeholder(Slots.RowOneSlotFive, itemStack(Material.RED_WOOL) {
                        meta {
                            name = "<red>Port nicht verfügbar".deserializeMini()

                            setLore {
                                +"<red>Der Port ist nicht verfügbar, wähle einen anderen".deserializeMini()
                            }
                        }
                    })
                }

                if (createServer) {
                    pageChanger(Slots.RowFiveSlotNine, itemStack(Material.GREEN_DYE) {
                        meta {
                            name = "<green>Server erstellen".deserializeMini()

                            setLore {
                                +"<green>Erstelle den Server, das kann etwas dauern".deserializeMini()
                            }
                        }
                    }, 2, null) {
                        scope.launch {
                            val server = PterodactylAPI.createServer(
                                name = setName!!,
                                port = port!!,
                                memory = setMemory!!,
                                cpuPercentage = setCPU!!,
                                storage = setDisk!!,
                                isPrivate = createPrivateServer
                            ).first

                            createPrivateServer = false

                            delay(1.seconds)

                            it.player.openAdminGUI(true)

                            if (server == null) return@launch

                            val velocityServerIdentifier = pluginConfig.getVelocityServerIdentifier()

                            if (velocityServerIdentifier != null) {
                                PterodactylAPI.addProxyServer(server.idLong, port)
                            }
                        }
                    }

                    return@page
                }
            }

            page(2) {
                transitionTo = PageChangeEffect.SLIDE_VERTICALLY
                transitionFrom = PageChangeEffect.SLIDE_VERTICALLY

                placeholder(Slots.All, itemStack(Material.GRAY_STAINED_GLASS_PANE) {
                    meta {
                        name = "<gray>Warte...".deserializeMini()

                        setLore {
                            +"<gray>Der Server wird erstellt".deserializeMini()
                        }
                    }
                })
            }
        })
    }
}