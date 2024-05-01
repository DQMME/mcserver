package de.dqmme.mcserver.gui

import com.mattmalec.pterodactyl4j.UtilizationState
import com.mattmalec.pterodactyl4j.client.entities.Utilization
import de.dqmme.mcserver.dataclass.Server
import de.dqmme.mcserver.item.Skulls
import de.dqmme.mcserver.util.Colors
import de.dqmme.mcserver.util.coloredName
import de.dqmme.mcserver.util.deserializeMini
import net.axay.kspigot.items.addLore
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.setLore
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object GUIItems {
    //Skulls
    val scrollUp = with(Skulls.arrowUp.clone()) {
        meta {
            name = "<green>Hochscrollen".deserializeMini()

            setLore {
                +"<green>Scrolle hoch".deserializeMini()
            }
        }
        this
    }

    val scrollDown = with(Skulls.arrowDown.clone()) {
        meta {
            name = "<green>Runterscrollen".deserializeMini()

            setLore {
                +"<green>Scrolle runter".deserializeMini()
            }
        }
        this
    }

    val back = with(Skulls.arrowLeft.clone()) {
        meta {
            name = "<gold>Zurück".deserializeMini()

            setLore {
                +"<gold>Gehe zurück".deserializeMini()
            }
        }
        this
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

    fun serverItem(server: Server, utilization: Utilization?): ItemStack {
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

        if(utilization.state != UtilizationState.RUNNING) {
            return itemStack(stateItems[utilization.state]!!) {
                meta {
                    name = "<gold>${server.name}".deserializeMini()

                    addLore {
                        +"<yellow>Status: ${utilization.state.coloredName()}".deserializeMini()
                        +"<red>Du kannst dich nur mit laufenden Servern verbinden".deserializeMini()
                    }
                }
            }
        }

        return itemStack(stateItems[utilization.state]!!) {
            meta {
                name = "<gold>${server.name}".deserializeMini()

                addLore {
                    +"<yellow>Status: ${utilization.state.coloredName()}".deserializeMini()
                    +"<green>Klicke, um dich mit <gold>${server.name} <green>zu verbinden".deserializeMini()
                }
            }
        }
    }
}