package de.dqmme.mcserver.gui.admingui

import com.mattmalec.pterodactyl4j.client.entities.ClientServer
import com.mattmalec.pterodactyl4j.client.entities.GenericFile
import com.mattmalec.pterodactyl4j.client.entities.Utilization
import de.dqmme.mcserver.api.PterodactylAPI
import de.dqmme.mcserver.gui.admingui.page.createServerPage
import de.dqmme.mcserver.gui.admingui.page.manageServersPage
import de.dqmme.mcserver.gui.openWaitGUI
import de.dqmme.mcserver.item.Skulls
import de.dqmme.mcserver.util.Database
import de.dqmme.mcserver.util.deserializeMini
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.axay.kspigot.gui.GUIType
import net.axay.kspigot.gui.PageChangeEffect
import net.axay.kspigot.gui.Slots
import net.axay.kspigot.gui.kSpigotGUI
import net.axay.kspigot.gui.openGUI
import net.axay.kspigot.items.itemStack
import net.axay.kspigot.items.meta
import net.axay.kspigot.items.name
import net.axay.kspigot.runnables.task
import org.bukkit.Material
import org.bukkit.entity.Player

val scope = CoroutineScope(Dispatchers.IO)

suspend fun Player.openAdminGUI(startManageServers: Boolean = false) {
    task(true) {
        openWaitGUI()
    }

    val servers = Database.getServers().toMutableList()
    val serverInfos = hashMapOf<Long, ClientServer>()
    val serverUtilization = hashMapOf<Long, Utilization>()
    val serverPlugins = hashMapOf<Long, List<GenericFile>>()

    servers.forEach {
        val clientServer = it.getClientServer() ?: return@forEach

        serverInfos[it.id] = clientServer

        val plugins = PterodactylAPI.getDirectory(clientServer, "plugins")?.files ?: return@forEach

        serverPlugins[it.id] = plugins

        val utilization = PterodactylAPI.getUtilization(clientServer) ?: return@forEach

        serverUtilization[it.id] = utilization
    }

    servers.removeIf { serverInfos[it.id] == null }

    task(true, delay = if(servers.isEmpty()) 15 else 0) {
        openGUI(kSpigotGUI(GUIType.FIVE_BY_NINE) {
            title = "<red>Admin-Menü".deserializeMini()

            page(if(!startManageServers) 1 else 2) {
                transitionTo = PageChangeEffect.SLIDE_VERTICALLY
                transitionFrom = PageChangeEffect.SLIDE_VERTICALLY

                pageChanger(Slots.RowThreeSlotFour, itemStack(Material.BOOK) {
                    meta {
                        name = "<green>Server verwalten".deserializeMini()
                    }
                }, if(startManageServers) 1 else 2, null, null)

                pageChanger(Slots.RowThreeSlotSix, Skulls.plus, 3, null, null)
            }

            manageServersPage(startManageServers, servers, serverInfos, serverUtilization, serverPlugins)

            createServerPage()
        })
    }
}