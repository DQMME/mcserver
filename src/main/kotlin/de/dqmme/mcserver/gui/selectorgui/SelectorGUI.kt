package de.dqmme.mcserver.gui.selectorgui

import com.mattmalec.pterodactyl4j.client.entities.ClientServer
import com.mattmalec.pterodactyl4j.client.entities.Utilization
import de.dqmme.mcserver.api.PterodactylAPI
import de.dqmme.mcserver.config.impl.navigatorConfig
import de.dqmme.mcserver.database.Database
import de.dqmme.mcserver.dataclass.NavigatorItem
import de.dqmme.mcserver.dataclass.Server
import de.dqmme.mcserver.gui.openWaitGUI
import de.dqmme.mcserver.gui.selectorgui.page.manageSelectorPage
import de.dqmme.mcserver.gui.selectorgui.page.privateServersPage
import de.dqmme.mcserver.gui.selectorgui.page.startPage
import de.dqmme.mcserver.util.Permissions
import de.dqmme.mcserver.util.deserializeMini
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

suspend fun Player.openSelectorGUI(startManagePage: Boolean = false) {
    openWaitGUI()

    val servers = Database.getNavigatorServers()

    val privateServers = if (hasPermission(Permissions.JOIN_ALL_SERVERS)) {
        Database.getPrivateServers()
    } else {
        Database.getPrivateServers(uniqueId.toString())
    }

    val items = hashMapOf<Server, NavigatorItem>()

    servers.forEach {
        val slot = navigatorConfig.getServerItem(it.id) ?: return@forEach
        items[it] = slot
    }

    val serverInfos = hashMapOf<Long, ClientServer>()
    val privateServerInfos = hashMapOf<Long, ClientServer>()
    val privateServerUtilizations = hashMapOf<Long, Utilization>()

    privateServers.forEach {
        val serverInfo = it.getClientServer() ?: return@forEach

        val utilization = PterodactylAPI.getUtilization(serverInfo)

        privateServerInfos[it.id] = serverInfo
        if (utilization != null) privateServerUtilizations[it.id] = utilization
    }

    items.forEach {
        val serverInfo = it.key.getClientServer()

        if (serverInfo != null) serverInfos[it.key.id] = serverInfo
    }

    val serverUtilizations = hashMapOf<Long, Utilization>()

    serverInfos.forEach {
        val utilization = PterodactylAPI.getUtilization(it.value)

        if (utilization != null) serverUtilizations[it.key] = utilization
    }

    task(true) {
        openGUI(kSpigotGUI(GUIType.SIX_BY_NINE) {
            startPage(this@openSelectorGUI, items, serverInfos, serverUtilizations, startManagePage)

            manageSelectorPage(items, startManagePage)

            privateServersPage(privateServers, privateServerInfos, privateServerUtilizations)

            page(3) {
                transitionTo = PageChangeEffect.SLIDE_VERTICALLY
                transitionFrom = PageChangeEffect.SLIDE_VERTICALLY

                placeholder(Slots.All, itemStack(Material.GRAY_STAINED_GLASS_PANE) {
                    meta {
                        name = "<gray>Warte...".deserializeMini()

                        setLore {
                            +"<gray>Das Item wird gelöscht".deserializeMini()
                        }
                    }
                })
            }
        })
    }
}