package de.dqmme.mcserver.gui

import com.mattmalec.pterodactyl4j.UtilizationState
import de.dqmme.mcserver.item.Skulls
import de.dqmme.mcserver.util.Colors
import de.dqmme.mcserver.util.deserializeMini
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.setLore
import org.bukkit.Material

object GUIItems {
    //Skulls
    val scrollUp = with(Skulls.arrowUp) {
        meta {
            name = "<green>Hochscrollen".deserializeMini()

            setLore {
                +"<green>Scrolle hoch".deserializeMini()
            }
        }
        this
    }

    val scrollDown = with(Skulls.arrowDown) {
        meta {
            name = "<green>Runterscrollen".deserializeMini()

            setLore {
                +"<green>Scrolle runter".deserializeMini()
            }
        }
        this
    }

    val back = with(Skulls.arrowLeft) {
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
}