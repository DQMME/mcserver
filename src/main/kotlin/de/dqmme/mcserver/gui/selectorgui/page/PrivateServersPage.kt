package de.dqmme.mcserver.gui.selectorgui.page

import com.mattmalec.pterodactyl4j.UtilizationState
import com.mattmalec.pterodactyl4j.client.entities.ClientServer
import com.mattmalec.pterodactyl4j.client.entities.Utilization
import de.dqmme.mcserver.dataclass.Server
import de.dqmme.mcserver.item.Items.stateItems
import de.dqmme.mcserver.item.Skulls
import de.dqmme.mcserver.util.coloredName
import de.dqmme.mcserver.util.deserializeMini
import net.axay.kspigot.gui.ForInventorySixByNine
import net.axay.kspigot.gui.GUIBuilder
import net.axay.kspigot.gui.PageChangeEffect
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.items.addLore
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.items.setLore
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

fun GUIBuilder<ForInventorySixByNine>.privateServersPage(
    privateServers: List<Server>,
    privateServerInfos: HashMap<Long, ClientServer>,
    privateServerUtilizations: HashMap<Long, Utilization>,
    openManageServers: Boolean = false
) {
    page(5) {
        transitionTo = PageChangeEffect.SLIDE_VERTICALLY
        transitionFrom = PageChangeEffect.SLIDE_VERTICALLY

        val compound = createRectCompound<Server>(
            Slots.RowOneSlotOne, Slots.RowSixSlotEight,
            iconGenerator = iconGenerator@{
                if (privateServerInfos[it.id] == null) {
                    return@iconGenerator itemStack(Material.BARRIER) {
                        meta {
                            name = "<red>Nicht gefunden".deserializeMini()

                            setLore {
                                +"<red>Mit dem Server ist ein Problem aufgetreten".deserializeMini()
                            }
                        }
                    }
                }

                val utilization = privateServerUtilizations[it.id]

                serverItem(it, utilization)
            },
            onClick = onClick@{ clickEvent, element ->
                clickEvent.bukkitEvent.isCancelled = true

                val serverInfo = privateServerInfos[element.id] ?: return@onClick
                val utilization = privateServerUtilizations[element.id] ?: return@onClick

                if (utilization.state != UtilizationState.RUNNING) return@onClick

                clickEvent.player.sendMessage("wirst verbunden")
            })

        compound.sortContentBy { it.name }

        compound.addContent(privateServers)

        compoundScroll(
            Slots.RowThreeSlotNine, Skulls.arrowUp, compound, scrollTimes = 3, reverse = true
        )

        compoundScroll(
            Slots.RowOneSlotNine, Skulls.arrowDown, compound, scrollTimes = 3
        )

        pageChanger(
            Slots.RowThreeSlotOne,
            Skulls.arrowLeft,
            if (openManageServers) 2 else 1,
            null,
            null
        )
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

    return itemStack(stateItems[utilization.state]!!) {
        meta {
            name = "<gold>${server.name}".deserializeMini()

            addLore {
                +"<yellow>Status: ${utilization.state.coloredName()}".deserializeMini()
            }
        }
    }
}